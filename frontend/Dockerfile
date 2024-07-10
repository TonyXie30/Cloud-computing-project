# Use Node.js to build the project
FROM node:20.13.1 AS build
WORKDIR /app
COPY package*.json ./
COPY . .

RUN npm install -g pnpm@8.15.6
RUN pnpm config set registry https://registry.npm.taobao.org
RUN pnpm install
RUN pnpm run build

# Use Nginx to serve the built files
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html

# Copy Nginx configuration file to sites-enabled directory
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy SSL certificate and key
#COPY certificates/certificate.crt /etc/ssl/certs/certificate.crt
#COPY certificates/private.key /etc/ssl/certs/private.key

EXPOSE 80
EXPOSE 443
CMD ["nginx", "-g", "daemon off;"]
