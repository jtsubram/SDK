package com.cisco.activiti.delegate;

import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cisco.encryption.aes.EncryptionUtil;

public class MsgClub implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgClub.class);
    org.activiti.engine.delegate.Expression PhoneNumbers;
    static String retval = "";

    public void execute(DelegateExecution execution) throws Exception {
    	StringBuilder sbPostData ;
        String strEmergencyPhoneNumbers = "";
        Properties env = new Properties();
        Map<String, Object> variables = execution.getVariables();
        InputStream input = null;
        LOGGER.info(String.format("MsgClub:Sending SMS initiated by %s  ", variables.get("ownerName")));
        try {
        	input = new FileInputStream(System.getProperty("app.home")+"/application.properties");
        	env.load(input);
            StringBuilder message = new StringBuilder();
            if (variables.get("message") != null
                    && !variables.get("message").toString().isEmpty()) {
                message.append(variables.get("message"));
            } else {
	            if (variables.get("incidentName") != null) {
	                message.append(variables.get("incidentName"));
	            }
	            if (variables.get("description") != null) {
	                message.append(" Incident with description, " + variables.get("description"));
	            } else {
	                message.append("Incident");
	            }
	            if (variables.get("severity") != null) {
	                message.append(", with severity " + variables.get("severity"));
	            }
	            message.append(", Has been raised");
	            if (variables.get("location") != null) {
	                message.append(" at Location, " + variables.get("location"));
	            }
            }
            if (variables.get("PhoneNumbers") != null) {
                strEmergencyPhoneNumbers = variables.get("PhoneNumbers").toString();
            } else {
                strEmergencyPhoneNumbers = (String) PhoneNumbers.getValue(execution);
            }
            if (strEmergencyPhoneNumbers != null && !strEmergencyPhoneNumbers.isEmpty()) {
    			sbPostData = new StringBuilder();
    			sbPostData.append("{\"smsContent\":\"");
    			sbPostData.append(message.toString());
    			sbPostData.append("1");
    			sbPostData.append("\",\"mobileNumbers\":\"");
    			sbPostData.append(strEmergencyPhoneNumbers);
    			sbPostData.append("\",\"senderId\":\"");
    			sbPostData.append(env.getProperty(variables.get("tenantId") + "_activiti.sms.username"));
    			sbPostData.append("\",\"signature\":");
    			sbPostData.append("\"signature\"");
    			sbPostData.append(",\"smsContentType\":");
    			sbPostData.append("\"english\"}");
                String authkey = EncryptionUtil.decrypt(
                        env.getProperty(variables.get("tenantId") + "_activiti.sms.password"));
    			//String authkey = env.getProperty(variables.get("tenantId") + "_activiti.sms.password");
                URL url = new URL(env.getProperty(variables.get("tenantId") + "_activiti.sms.url")+authkey);
                HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
                urlconnection.setRequestMethod("POST");
                urlconnection.setRequestProperty("Content-Type", "application/json");
                urlconnection.setDoOutput(true);
                OutputStreamWriter out = new OutputStreamWriter(urlconnection.getOutputStream());
                out.write(sbPostData.toString());
                out.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(urlconnection.getInputStream()));
                String decodedString;
                while ((decodedString = in.readLine()) != null) {
                    retval += decodedString;
                }
                LOGGER.info(retval);
                in.close();
            }
        } catch (Exception e) {
            LOGGER.error("MsgClub while sending SMS " + e.getMessage() + " for phone numbers "
                    + strEmergencyPhoneNumbers, e);
        }finally {
    		if (input != null) {
    			try {
    				input.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    }
}
