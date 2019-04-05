# SupportBot [![Discord](https://img.shields.io/discord/398215838668161024.svg?style=for-the-badge)](https://discord.gg/Pf9Xn9C)

SupportBot is the multi-purpose, user friendly Discord bot for the [NetworkSync](https://networksync.co.uk/) public 

## Usage

To use, download the source and compile it using gradle, and create the following script and run it in the same directory as the jar file. 

```bat
@echo off
java -jar discordbot-1.0.0.jar
pause
```
## Setup
After running it for the first time, edit the config file with the needed information. All fields are required in order for the bot to run successfully. To get your token, create a Discord application [here](https://discordapp.com/developers/applications).  After creating the application, turn it into a bot account by clicking in the following locations.

![Bot Settings](https://i.yourmcgeek.ga/p9w9w.png 'Bot Settings Location')
![alt text](https://i.yourmcgeek.ga/xn56n.png 'Build a bot')

From there, copy your token and add it to the config. Be mindful never to share your token with anyone! You'll need to invite the bot to your guild(s) of choice. To do so, copy the client id found under `General Information` in the Settings panel, and replace it in the following URL.
```
https://discordapp.com/oauth2/authorize?client_id=INSERT_CLIENT_ID_HERE&scope=bot&permissions=8
```
In order for the bot to run, you will need to have [2-Factor Authentication](https://support.discordapp.com/hc/en-us/articles/219576828-Setting-up-Two-Factor-Authentication) enabled.

If you would like the bot to provide insights based on peoples chat, you'll need to edit the following array in your config file.
```JSON
   "tips": [  
      {  
         "suggestion":"If you need help with an issue related to the plugin run the `<prefix>help` command!",
         "word":"help"
     },
     {  
         "suggestion":"This is a test!",
         "word":"test"
     }
   ]
```
Follow the format in order to add more words that the bot will respond to with the corresponding message. 

### Placeholders

|      Placeholder 	    |                                    Value                                    |
|    :------------:	    |  :-----------------------------------------------------------------------:  |
|    `<githubissues>`   	|                          https://github.com/NetworkSync/NetBans/issues/new      |
|   ` <prefix> `         |                         Prefix from config                        	      |
|    `<github>	 `   |                     https://github.com/NetworkSync                     	      |
|    `<tag> `    	        |                              Tags the user                                  |

### Credit

The bot is built on the public Discord API known as [JDA](https://github.com/DV8FromTheWorld/JDA) with the 3rd party library of [bJdaUtilities](https://github.com/bhopahk/bJdaUtilities/)

### Related Links
* [NetworkSync Github Repo](https://github.com/networksync)

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[GPL 3.0](https://github.com/networksync/SupportBot/blob/master/LICENSE)