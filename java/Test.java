import java.io.IOException;
import java.util.ArrayList;
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
			
			
			Orchestrator orch = new Orchestrator("tenantName", "admin", "passwd", "https://uipath.myrobots.co.kr/");
			
			
			/*________________________________________________________________*/
			/*                           SEND REQUEST                         */
			/* Params (Hash): request type, url extension, [data], [callback] */
			/*________________________________________________________________*/
			
			
			String folderId = null;
			ArrayList<Map> folders = orch.getFolders();
			for( Map m : folders)
			{
				if( m.get("DisplayName").toString().equals("PRESALES"))
					folderId = m.get("Id").toString();
				System.out.println( m.get("DisplayName").toString() + " has " + m.get("Id").toString());
			}
			Map res;
			
		
			JsonObject body = new JsonObject();
			JsonObject item = new JsonObject();
			item.addProperty("Name", "CharlesQ");
			JsonObject specific = new JsonObject();
			specific.addProperty("UserName", "UiPath코리아");
			specific.addProperty("Address", "서울 종로 종로 33 그랑서울 7층");
			specific.addProperty("Email", "korea@uipath.com");
			item.add("SpecificContent", specific);
			body.add("itemData", item);
			
			res = orch.AddQueueItem( body.toString(),  folderId);
			
			/*
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
			*/
			
		} catch (AuthenticationException | IOException | JsonSyntaxException  e) { e.printStackTrace(); }
	}
}
