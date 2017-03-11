# chaincode

## build

```
go get ./...
go build
```

## deployment


## deploying to your peers

instructions for private repo https://github.com/hyperledger-archives/fabric/issues/873:

```
Step 1:
Create a github personal access token with control of the private repository.

Step 2:
Change the git config settings on the VM that is running the peer :

GITHUB_ACCESS_TOKEN="tokenFromStep1"
git config --global url."https://${GITHUB_ACCESS_TOKEN}:x-oauth-basic@github.com/".insteadOf "https://github.com/"
This setting will cause git to substitute https://github.com with https://tokenFromStep1:x-oauth-basic@github.com/ when cloning.

When go get is run by the peer, git will do the correct substitution and successfully clone your private repository and install the chaincode.

This needs to be done on all of the validating peers. Peers without this config will not be able to find the chaincode and will break consensus.
```

## alternative

(does not work: only support https URL)
* go on the peer
* ssh-keygen
* take public key and add it as deploy key in the repo