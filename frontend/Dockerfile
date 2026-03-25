# Etapa 1: Construir la aplicación con Node.js
FROM node:20-alpine AS build

WORKDIR /app

# Copiar configuración y dependencias
COPY package.json package-lock.json* ./
RUN npm ci

# Copiar código fuente y construir
COPY . .
RUN npm run build

# Etapa 2: Servir la aplicación estática construida con Nginx
FROM nginx:alpine

# Copiar la configuración personalizada de Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copiar los estáticos generados por Vite
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
