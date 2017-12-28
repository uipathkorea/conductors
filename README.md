# Conductors
So. You have an Orchestrator but you're one of those tech weirdies who doesn't like UIs. Maybe you're an exclusively backdoor kinda person whose chin drips with drool at the sight of a shapely API. I don't know--that's between you and your shrink. But while you figure all that out, I'll be writing up wrappers and examples to help you get off

the ground with the Orchestrator API. Try to contain your excitement.

To use:
1. Select whichever language makes your eyes twinkle
2. Insert the *orchestrator* file into your pwd
3. Mimic the sample calls made in the *test* file
(No need to authenticate. This happens automatically)
***

## Javascript

#### Connect to Orchestrator
```javascript
require('./orchestrator.js');

var orch = new Orchestrator("tenant", "username", "password");
```

#### Make Calls
```javascript
// GET
orch.request({ type: "GET", 
               extension: 'odata/Environments',
               callback: printResult });

// POST
orch.request({ type: "POST", 
               extension: 'odata/Assets',
               body: JSON.stringify({ Name: "Caesar", ValueScope: "Global" }),
               callback: printResult });

// Callback
function printResult(response) {
	console.log(response);
}
```

## Python

#### Connect to Orchestrator
```python
from orchestrator import Orchestrator

orch = Orchestrator("tenant", "username", "password")
```

#### Make Calls
```python
# GET
res = orch.request('get', 'odata/Environments')
print(res)

# POST
res = orch.request('post', 'odata/Assets', {'Name': "Caesar",
					    'ValueScope': "Global",
					    'ValueType': "Text",
  					    'StringValue': "Et tu asset 2"})
print(res)
```

## Ruby

#### Connect to Orchestrator
```ruby
require_relative 'orchestrator'

orch = Orchestrator.new("tenant", "user", "password")
```

#### Make Calls
```ruby
# GET
response = orch.request('get', 'odata/Environments')
puts response["body"]["value"]

# POST
response = orch.request('post', 'odata/Assets', {Name: "Caesar",
                                                 ValueScope: "Global",
                                                 ValueType: "Text",
                                                 Value: "Et tu asset 2",
                                                 StringValue: "Et tu asset 2"})
puts response["body"]
```

