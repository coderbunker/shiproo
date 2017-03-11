export GITHUB_ACCESS_TOKEN=`cat personal_access_token`
peer network login test_user0 -p MS9qrN8hFjlE
git config --global url."https://${GITHUB_ACCESS_TOKEN}:x-oauth-basic@github.com/".insteadOf "https://github.com/"
peer chaincode deploy -u test_user0 -p https://github.com/freightcrunch/hackathon/chaincode -c '{ "function": "init", "args": ["hi there"] }'
