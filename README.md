# SDK
SDK to create custom code for task service in CKC SOP, this is for creating custom specific java delegate to be used by 
SOP workflow.

Step1:

Import the java project.

Step2:

Create a new java file under com.cisco.activiti.delegate package implement org.activiti.engine.delegate.JavaDelegate interface override the “execute” method with your implementation.Maintain application property file path as app.home and name as application.properties for externalizing the properties.Write junit under src/test/java to test the same. Passwords has to be encrypted and stored in property file and same has to be decrypted using EncryptionUtil API.

EncryptionUtil.decrypt(env.getProperty(variables.get("tenantId")+"_activiti.sms.password"));

public void execute(DelegateExecution execution) throws Exception { ----------------------- } FileInputStream(System.getProperty("app.home")+"/application.properties");

Step3:

Right click the project -> Run As -> Maven install this will create a customdelegate-1.0.jar file under target folder.

Step4:

During deployment copy the jar file to a folder and start the Activiti service with the jar file in its classpath using “loader.path” as in the below command. java -Xms256m -Xmx512m -Dlog4j.configurationFile=log4j2.xml -Dapp.home=. -Dloader.path=/workspace_trunk/CustomDelegate/target/ -jar activiti-4.0.1.jar

Note: The attached SDK has the sample implementation under src/main/java (com.cisco.activiti.delegate.msgClub.java ) and junit under src/test/java (com.cisco.activiti.delegate.MsgClubTest.java). Use the logger as shown in the sample.
