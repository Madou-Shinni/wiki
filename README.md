### Nginx 配置websocket线上无法连接的解决方案
```nginx configuration
server {
        listen       80;
        server_name  server.yumclor.com;

        location / {
            proxy_pass http://localhost:8081;
            # 以下是线上部署无法连接websocket的解决方案
            proxy_redirect off;
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "upgrade";
            proxy_set_header Host $host:$server_port;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }
}
```
