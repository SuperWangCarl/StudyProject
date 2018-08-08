/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.itcast.storm.kafka

import org.I0Itec.zkclient.ZkClient
import kafka.common.TopicAndPartition
import kafka.utils.{Pool, CoreUtils, ZkUtils, Logging}

import scala.collection.mutable

trait PartitionAssignor {

  /**
   * Assigns partitions to consumer instances in a group.
   * @return An assignment map of partition to this consumer group. This includes assignments for threads that belong
   *         to the same consumer group.
   */
  def assign(ctx: AssignmentContext): Pool[String, mutable.Map[TopicAndPartition, ConsumerThreadId]]

}

object PartitionAssignor {
  def createInstance(assignmentStrategy: String) = assignmentStrategy match {
    case "roundrobin" => new RoundRobinAssignor()
    case _ => new RangeAssignor()
  }
}

class AssignmentContext(group: String, val consumerId: String, excludeInternalTopics: Boolean, zkUtils: ZkUtils) {
  val myTopicThreadIds: collection.Map[String, collection.Set[ConsumerThreadId]] = {
    val myTopicCount = TopicCount.constructTopicCount(group, consumerId, zkUtils, excludeInternalTopics)
    myTopicCount.getConsumerThreadIdsPerTopic
  }

  val partitionsForTopic: collection.Map[String, Seq[Int]] =
    zkUtils.getPartitionsForTopics(myTopicThreadIds.keySet.toSeq)

  val consumersForTopic: collection.Map[String, List[ConsumerThreadId]] =
    zkUtils.getConsumersPerTopic(group, excludeInternalTopics)

  val consumers: Seq[String] = zkUtils.getConsumersInGroup(group).sorted
}

/**
 * The round-robin partition assignor lays out all the available partitions and all the available consumer threads. It
 * then proceeds to do a round-robin assignment from partition to consumer thread. If the subscriptions of all consumer
 * instances are identical, then the partitions will be uniformly distributed. (i.e., the partition ownership counts
 * will be within a delta of exactly one across all consumer threads.)
 *
 * (For simplicity of implementation) the assignor is allowed to assign a given topic-partition to any consumer instance
 * and thread-id within that instance. Therefore, round-robin assignment is allowed only if:
 * a) Every topic has the same number of streams within a consumer instance
 * b) The set of subscribed topics is identical for every consumer instance within the group.
 */

class RoundRobinAssignor() extends PartitionAssignor with Logging {

  def assign(ctx: AssignmentContext) = {

    val valueFactory = (topic: String) => new mutable.HashMap[TopicAndPartition, ConsumerThreadId]
    val partitionAssignment =
      new Pool[String, mutable.Map[TopicAndPartition, ConsumerThreadId]](Some(valueFactory))

    if (ctx.consumersForTopic.size > 0) {
      // check conditions (a) and (b)
      val (headTopic, headThreadIdSet) = (ctx.consumersForTopic.head._1, ctx.consumersForTopic.head._2.toSet)
      ctx.consumersForTopic.foreach { case (topic, threadIds) =>
        val threadIdSet = threadIds.toSet
        require(threadIdSet == headThreadIdSet,
          "Round-robin assignment is allowed only if all consumers in the group subscribe to the same topics, " +
            "AND if the stream counts across topics are identical for a given consumer instance.\n" +
            "Topic %s has the following available consumer streams: %s\n".format(topic, threadIdSet) +
            "Topic %s has the following available consumer streams: %s\n".format(headTopic, headThreadIdSet))
      }

      val threadAssignor = CoreUtils.circularIterator(headThreadIdSet.toSeq.sorted)

      info("Starting round-robin assignment with consumers " + ctx.consumers)
      val allTopicPartitions = ctx.partitionsForTopic.flatMap { case (topic, partitions) =>
        info("Consumer %s rebalancing the following partitions for topic %s: %s"
          .format(ctx.consumerId, topic, partitions))
        partitions.map(partition => {
          TopicAndPartition(topic, partition)
        })
      }.toSeq.sortWith((topicPartition1, topicPartition2) => {
        /*
         * Randomize the order by taking the hashcode to reduce the likelihood of all partitions of a given topic ending
         * up on one consumer (if it has a high enough stream count).
         */
        topicPartition1.toString.hashCode < topicPartition2.toString.hashCode
      })

      allTopicPartitions.foreach(topicPartition => {
        val threadId = threadAssignor.next()
        // record the partition ownership decision
        val assignmentForConsumer = partitionAssignment.getAndMaybePut(threadId.consumer)
        assignmentForConsumer += (topicPartition -> threadId)
      })
    }

    // assign Map.empty for the consumers which are not associated with topic partitions
    ctx.consumers.foreach(consumerId => partitionAssignment.getAndMaybePut(consumerId))
    partitionAssignment
  }
}

/**
 * Range partitioning works on a per-topic basis. For each topic, we lay out the available partitions in numeric order
 * and the consumer threads in lexicographic order. We then divide the number of partitions by the total number of
 * consumer streams (threads) to determine the number of partitions to assign to each consumer. If it does not evenly
 * divide, then the first few consumers will have one extra partition. For example, suppose there are two consumers C1
 * and C2 with two streams each, and there are five available partitions (p0, p1, p2, p3, p4). So each consumer thread
 * will get at least one partition and the first consumer thread will get one extra partition. So the assignment will be:
 * p0 -> C1-0, p1 -> C1-0, p2 -> C1-1, p3 -> C2-0, p4 -> C2-1
 */
class RangeAssignor() extends PartitionAssignor with Logging {

  def assign(ctx: AssignmentContext) = {
    val valueFactory = (topic: String) => new mutable.HashMap[TopicAndPartition, ConsumerThreadId]
    val partitionAssignment =
      new Pool[String, mutable.Map[TopicAndPartition, ConsumerThreadId]](Some(valueFactory))
    for (topic <- ctx.myTopicThreadIds.keySet) {

      //获取当前的消费者数量  假设为5个消费者
      val curConsumers = ctx.consumersForTopic(topic)
      //获取当前的分区数量 假设为4个分区
      val curPartitions: Seq[Int] = ctx.partitionsForTopic(topic)

      //计算一个整除的 得到  nPartsPerConsumer = 0
      val nPartsPerConsumer = curPartitions.size / curConsumers.size
      //计算取模之后的值 nConsumersWithExtraPart = 4
      val nConsumersWithExtraPart = curPartitions.size % curConsumers.size

      //迭代出所有的消费者线程，并计算这个消费者管理分分片区间
      for (consumerThreadId <- curConsumers) {
        //获取消费者的位置 0
        val myConsumerPosition = curConsumers.indexOf(consumerThreadId)
        assert(myConsumerPosition >= 0)

        //计算C0的startPart 等于  0*0 + 0   = 0
        //计算C1的startPart 等于  0*1 + 1  = 1
        //计算C2的startPart 等于 0*2 + 2 = 2
        //计算C3的startPart 等于 0*3 + 3 = 3
        //计算C4的startPart 等于 0*4 + 4 = 4
        val startPart = nPartsPerConsumer * myConsumerPosition + myConsumerPosition.min(nConsumersWithExtraPart)
        //计算C0的nParts 等于 0 + 1 = 1
        //计算C1的startPart 等于 0+ 1 =1
        //计算C2的startPart 等于 0+ 1  =1
        //计算C3的startPart 等于 0+ 1  =1
        //计算C4的startPart 等于 0+ 0  =0
        val nParts = nPartsPerConsumer + (if (myConsumerPosition + 1 > nConsumersWithExtraPart) 0 else 1)
        //取值范围  startPart until startPart + nParts
        // C0的取值范围是 0 until 1，对应的分片是p0
        // C1的取值范围是 1 until 2,对应分片是p1
        // C2的取值范围是 2 until 3 对应分片是p2
        // C3的取值范围是 3 until 4 对应分片是p3
        // C4的取值范围是 4 until 4 对应分片是 空值

        /**
         *   Range-partition the sorted partitions to consumers for better locality.
         *  The first few consumers pick up an extra partition, if any.
         */
        if (nParts <= 0)
          warn("No broker partitions consumed by consumer thread " + consumerThreadId + " for topic " + topic)
        else {
          //for(i <-  0 until 3 )  ------------0,1,2
          //for(i <- 3 until 5)    ------------3,4
          for (i <- startPart until startPart + nParts) {
            val partition = curPartitions(i)
            info(consumerThreadId + " attempting to claim partition " + partition)
            // record the partition ownership decision
            val assignmentForConsumer = partitionAssignment.getAndMaybePut(consumerThreadId.consumer)
            assignmentForConsumer += (TopicAndPartition(topic, partition) -> consumerThreadId)
          }
        }
      }
    }

    // assign Map.empty for the consumers which are not associated with topic partitions
    ctx.consumers.foreach(consumerId => partitionAssignment.getAndMaybePut(consumerId))
    partitionAssignment
  }
}
