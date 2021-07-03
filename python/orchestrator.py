import requests
import json

class Orchestrator:

	def __init__(self, tenant, user, password, url = 'https://uipath.myrobots.co.kr/'):
		if url.endswith('/'):
			self.url = url[:-1]
		else:
			self.url = url
		self.headers = {'Authorization': 'Bearer ', 'Content-Type': 'application/json'}
		self.token = self.__getToken(tenant, user, password)
		if self.token:
			self.headers["Authorization"] = "Bearer {0}".format(self.token)


	def __getToken(self, tenant, user, password):
		res = self.request('POST', '/api/account/authenticate', {'tenancyName': tenant,
                                                                       'usernameOrEmailAddress': user, 
                                                                       'password': password})
		return res["result"]

	def __getFolderId(self, folder_name):
		response = requests.request( 'GET', self.url + "/odata/Folders?%24filter=DisplayName%20eq%20'{0}'&%24select=Id".format( folder_name), data=None, headers=self.headers)
		if response.status_code == 200:
			folder = response.json()
			return str(folder["value"][0]["Id"])
		else:
			return None


	def add_queue_item(self, folder_name, body):
		fid = self.__getFolderId(folder_name)
		self.headers['X-UIPATH-OrganizationUnitId'] = fid
		uri = self.url + '/odata/Queues/UiPathODataSvc.AddQueueItem'
		response = requests.request('POST', uri, data=json.dumps(body,ensure_ascii=True), headers=self.headers)
		if response.status_code in [200,201]:
			return response.json()
		else:
			return None
  	
	def request(self, type, extension, body = None, folder = None):
		uri = self.url + extension
		if folder:
			fid = self.__getFolderId(folder)
			self.headers['X-UIPATH-OrganizationUnitId'] = fid
		response = requests.request(type.upper(), uri, data = json.dumps(body) if body else None, headers=self.headers)
		return response.json()
