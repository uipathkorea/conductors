from orchestrator import Orchestrator
import json

#________________________________________________________________#
#                    CREATE ORCHESTRATOR OBJECT                  #
#                  (Automatically authenticates)                 #
#     Params: tenant, username, password, [orchestrator url]     #
#________________________________________________________________#

orch = Orchestrator("charles", "42maru", "수정한 패스워드 ", "https://uipath.myrobots.co.kr")

#________________________________________________________________#
#                          SEND REQUEST                          #
#          Params: request type, url extension, [data]           #
#________________________________________________________________#

entities = {
	"name" : "SK텔레콤",
	"date": "2021-07-03",
	"variable": "다른 변수의 값",
	"items": {
		"변수1": "값1",
		"변수2" : "값2"
	}
}

entity_json_str = json.dumps( entities, ensure_ascii=False)
print(entity_json_str)
# POST
res = orch.add_queue_item( "42maru", {
  "itemData": {
    "Name": "chatbot_42maru",
    "Priority": "High",
    "SpecificContent": { 
		"intent": "my intent",
		"entities": entity_json_str,
		"user_id": "hyungsoo.kim@uipath.com",
		"session_id": "x72llk23423xxxxs"
        },
    "Reference": "Intent Name"
  }
})
print(res)