[![Download ](https://img.shields.io/badge/-Download%20APK-brightgreen)](https://github.com/yashkasera/WatSights/raw/main/app/release/app-release.apk)

## WatSights

Missing out on important messages due to spamming of unwanted messages? Missed out an important deadline because of someone who can't stop talking about what they had for dinner? Wasting your precious time to save a number before texting them knowing fully well you won't be contacting them anywhere in the future? Not anymore.

Here we present to you WatSights - A one stop solution to all your Whatsapp problems. Be it missing out on important messages, sending whatsapp messages to unknown numbers, clearing junk storage occupied by stickers, downloading someone's status or recovering a deleted message. WatSights got you covered.

[![DOCS](https://img.shields.io/badge/Source%20Code-see%20docs-green)](https://github.com/yashkasera/WatSights)

[![DOCS](https://img.shields.io/badge/Website-WatSightsWeb-orange)](https://yashkasera.github.io/WatSightsWeb)

[![UI ](https://img.shields.io/badge/User%20Interface-Link%20to%20UI-orange?style=flat-square&logo=appveyor)](https://www.figma.com/file/CbHs8D3vRu8WADaS0EV6NW/watsights-updated?node-id=0%3A1)


# Features
* Important Messages - Browse through your important messages that you might have missed during a conversation
* Direct Message - Now conveniently text an unknown number on whatsapp without saving it.
* Elite Members - Select important contacts as Elite Members and directly view their messages
* Storage - One stop destination for all your whatsapp media
* Notifications - Get notified when someone sends an important message or mentions you, so you dont miss out on something important
* Deleted Media - Someone sent you an image and deleted it. Annoying right? You can now see what they sent through this app

# How it works?
WatSights scans your message notifications and stores them locally in a database on your device. This database is only accessible to you through this app and no one (not even other apps on your phone) has access to those messages. When someone sends you a message, you get a notification. This app reads the text in the notification and determines if the message is important or not using keywords. It searches for possible combinations like links, phone numbers, email addresses, meeting links, etc.

Similarly it listens for media changes when a new media is added on your phone and if the media is deleted, it is copied in a folder on your phone. So even if the person deletes it, you still have access to that media.

# Permissions Required
In order for this app to work properly, we require a few permissions.
* Read Notifications : The main objective of this app is to filter out important messages from the spam ones. In order to read those messages, this app requires notification access.
* Read external Storage : Since this app works by listening to incoming media, we need access to read your storage
* Write external Storage : When a media is deleted, we have to store it to a folder so we need write permissions
* Manage external Storage : This app lets you to view Whatsapp media and delete pointless stickers and gifs, we need access to manage your data. This does not let the app delete anything without your permission

**What we don't require from you**
* Internet : Yes, we do not require internet. This app works completely offline for you to feel safe. We have not included any third party ads to ensure your privacy.


# Our Team

- Yash Kasera - Application/Web Developer
- Aarushi Talwar - Frontend Designer
- Aditya Malpani - Content Manager

# Contact Us
Have any queries, suggestions or complaints? We'd love to hear it from you.
Please feel free to reach us out at
[yashkasera@icloud.com](mailto:yashkasera@icloud.com)

