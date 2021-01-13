import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class Orchestrator {
	private String url;
	private static String token = "";
	
	public Orchestrator(String tenant, String user, String pass) throws AuthenticationException {
		this(tenant, user, pass, "https://platform.uipath.com");
	}
	
	public Orchestrator(String tenant, String user, String pass, String url) throws AuthenticationException {
		this.url = url.endsWith("/") ? url.substring(0, url.length()-1) : url;
		token = getToken(tenant, user, pass);
	}
	
	
	private String getToken(String tenant, String user, String pass) throws AuthenticationException {
		// Form body of request
		JsonObject body = new JsonObject();
		body.addProperty("tenancyName", tenant);
		body.addProperty("usernameOrEmailAddress", user);
		body.addProperty("password", pass);
		
		// Make request & handle errors
		Map res;
		try {
			res = requestWithFolderId("POST", "/api/account/authenticate", body.toString(), null);
		} catch (JsonSyntaxException e) {
			throw new AuthenticationException("Response was not JSON as expected.\n" + e.getMessage());
		} catch (IOException e) {
			String message = "Something went wrong...\n";
			if(e.getMessage().contains("code: 40"))
				message = "Incorrect endpoint. Check your url.\n";
			else if(e.getMessage().contains("code: 50"))
				message = "The server didn't like our request. Check your credentials.\n";
			
			throw new AuthenticationException( message + e.getMessage() );
		}
		
		// Return auth token
		return res.get("result").toString();
	}

	public ArrayList<Map> getFolders()  throws IOException, JsonSyntaxException {
		ArrayList<Map> folders = null; 
		Map res  = requestWithFolderId("GET", "/odata/Folders?%24select=DisplayName%2CFullyQualifiedName%2CId", null, null);
		if( Double.parseDouble( res.get("@odata.count").toString()) >= 1.0)
		{
			folders = (ArrayList<Map>)res.get("value");
			for( Map m : folders) {
				m.put( "Id", (int)(Double.parseDouble(m.get("Id").toString())));
			}
		}
		return folders;
	}

	public Map AddQueueItem ( String jsonBody, String folderId ) throws IOException, JsonSyntaxException {

		return requestWithFolderId( "POST", "/odata/Queues/UiPathODataSvc.AddQueueItem", jsonBody, folderId);
	}


	public Map StartJob ( String jsonBody, String folderId) throws IOException, JsonSyntaxException {
		return requestWithFolderId( "POST", "/odata/Jobs/UiPath.Server.Configuration.OData.StartJobs", jsonBody, folderId);
	}

	public Map GetJobStatus( String jobId, String folderId) throws IOException, JsonSyntaxException { 
		return requestWithFolderId( "GET",
			"/odata/Jobs("+jobId+")?%24select=ReleaseName%2CId%2CState%2COutputArguments%2CStartTime%2CEndTime",
			null, folderId);
	}


	private Map requestWithFolderId(String type, String extension, String body, String folderId ) throws IOException, JsonSyntaxException { 
		// Create Connection
		URL url = new URL(this.url + (extension.startsWith("/") ?  extension :  "/" + extension));
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		
		// Compose Request
		con.setRequestMethod(type.toUpperCase());
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", "Bearer "+ Orchestrator.token);
		con.addRequestProperty("User-Agent", "");
		if( folderId != null && folderId.length() != 0)
			con.addRequestProperty("X-UIPATH-OrganizationUnitId", folderId);
		con.setDoInput(true);
		con.setDoOutput(true);
		
		// Send Request
		if (body != null) {
			OutputStream os = con.getOutputStream();
			os.write(body.getBytes("UTF-8"));
			os.close();
		}
		
		// Get Response
		BufferedReader in = new BufferedReader(
                                    new InputStreamReader(con.getInputStream()));
		String line;
		StringBuffer content = new StringBuffer();
		while ((line = in.readLine()) != null)
		    content.append(line);
		in.close();
		con.disconnect();
		//System.out.println("Body : " + content.toString());
		// Parse Response to Map
		Map map = new Gson().fromJson(content.toString(), Map.class);
		return map;
	}

	public Map request(String type, String extension, String body, String folderId) throws IOException, JsonSyntaxException {
		return requestWithFolderId( type, extension, body, folderId);
	}

	
	public static void main(String[] args) {
	}

}
