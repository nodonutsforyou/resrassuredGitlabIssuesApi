# simple example of rest-assured.io framework to test gitlab issues API

The point of the test is to check base CRUD operations over issues. I did not have much free time to work on this assignment and had no prior experience with rest assured, so I did not spend much time doing a full coverage of API. I did only basic operations - one test case for each. And I did not make extensive checks for any calls.

## Run instructions
You can run the test with either with maven locally, or in a docker container
1) To run in maven
Declare environment variables:

        BASE_URL = "https://gitlab.com/api/v4";
        TOKEN - your secret GitLab API token
        PROJECTID - project id created already existing in gitlab

To run with maven simply run `mvn test`

2) To run with docker, update file `run.sh` with environment variables:

        BASE_URL = "https://gitlab.com/api/v4";
        TOKEN - your secret GitLab API token
        PROJECTID - project id created already existing in gitlab

Then build a container with `docker build .`. Afterward, you can run the test again by running a container

## Test description
The test is meant to clean data after itself. Prior to the test, it creates 4 issues. And after the test, it does a cleanup, deleting all the issues createdThere are 4 test cases:
1) simple test to create 1 new issue
2) getting a list of issues, and checking that issues we've created during this run is on this list
3) simple check that the update issue call returns correct statusCode
4) simple check of delete issue call

## My plan, or would I could've done nex
* Logging - currently there is a warning due to missing loggger implementation. It could be solved by attaching slf4j  dependency to maven
* deployment of gitlab - I spend some time trying to deploy Gitlab-ce image in docker-compose together with the test, but it would take some time to set this up in full
* More coverage - there is a lot to cover in issues functionality, so I've decided to not aim for this goal