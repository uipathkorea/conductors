import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import com.google.gson.JsonObject;
import com.google.gson.*;
import com.google.gson.JsonSyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;

public class Test {

	public static void main(String[] args) {
		try {
			Gson gson = new Gson();
			/*________________________________________________________________*/
			/*                    CREATE ORCHESTRATOR OBJECT                  */
			/*                  (Automatically authenticates)                 */
			/*              Params: tenant, username, password, [url]         */
			/*________________________________________________________________*/
			
			
			Orchestrator orch = new Orchestrator("lguplus", "username", "password", "https://uipath.myrobots.co.kr/", true);
			
			
			/*________________________________________________________________*/
			/*                           SEND REQUEST                         */
			/* Params (Hash): request type, url extension, [data], [callback] */
			/*________________________________________________________________*/
			
			
			Map res;
			
			
			JsonObject body = new JsonObject();
			JsonObject jbody = new JsonObject();
			jbody.addProperty("ReleaseKey", "583a2ce1-7d5a-4ba8-89ec-68129e249997");
			jbody.addProperty("Strategy", "Specific");
			jbody.addProperty("Source", "Manual");
			JsonArray robots = new JsonArray();
			robots.add( 112);
			jbody.add( "RobotIds", robots);
			jbody.addProperty("InputArguments", "{\"in_Message\": \"Test for Robots\"}");
			body.add( "startInfo", jbody);

			
			///odata/Jobs/UiPath.Server.Configuration.OData.StartJobs
			
			res = orch.request("post", "odata/Jobs/UiPath.Server.Configuration.OData.StartJobs", body.toString());
			
			
		} catch (AuthenticationException | IOException | JsonSyntaxException | NoSuchAlgorithmException | KeyManagementException e) { e.printStackTrace(); }
	}
}
