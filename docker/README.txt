1. Step 1 - install docker or boot2docker

2. Create postgres container
 - Option 1 - with volume container (for prod/qa/dev)
    docker create -v /var/lib/postgresql/data --name simple_db_vol postgres:9.4 /bin/true
    docker run --name simple_db -p 5432:5432 --volumes-from simple_db_vol -e POSTGRES_PASSWORD=test123 -e POSTGRES_USER=simple -d postgres:9.4
 - Option 2 - without volume container (for local development)
    docker run --name db -p 5432:5432  -e POSTGRES_PASSWORD=test123 -e POSTGRES_USER=simple -d postgres:9.4
3. Update your rest/application properties - JDBC configuration to match docker settings. Use your docker's URL in the jdbc.url string
4. Create nginx container
    docker run -d --name web -p 80:80 -v /Users/igoryarkov/projects/test/simple/docker/nginx.conf:/etc/nginx/nginx.conf:ro -v /Users/igoryarkov/projects/test/simple/web/src:/var/www:ro nginx


