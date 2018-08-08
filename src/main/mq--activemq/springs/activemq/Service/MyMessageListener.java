package springs.activemq.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListener implements MessageListener {
	public void onMessage(Message arg0) {
		// TODO Auto-generated method stub
		try {
			String message = ((TextMessage) arg0).getText();
			System.out.println("textmessage:" + message);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
