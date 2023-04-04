# day37
Day37 - Angular Upload

GOAL: 
1. Connect to SQL
2. Connect to S3
3. Upload Images to S3
4. Upload data to SQL


## Running Angular with proxy (base URL)
ng serve --proxy-config src/proxy.config.js


## running on vercel
1. ng cache clean

railway + vercel (need to add vercel.json file)
2. vercel.json (at client root folder)
```
{
  "rewrites": [
    {
      "source": "/api/:match(star)",
      "destination": "https://day39-production.up.railway.app/api/:match(star)"
    }
  ]
}
```