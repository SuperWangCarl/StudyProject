package springs.activemq.Service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

public class QueueConsumer {
	private static JmsTemplate jt = null;

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"config/ApplicationContext2.xml");
		//获取JmsTemplate对象
		jt = (JmsTemplate) ctx.getBean("jmsTemplate");
		//接收消息
		String msg = (String) jt.receiveAndConvert();
		System.out.println("receive msg = " + msg);
	}
}
