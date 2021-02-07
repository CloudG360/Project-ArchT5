# Project-ArchT5

## ARCHIVAL NOTICE!

This project is now abandoned as I've moved onto other projects and JDA has updated over time so, while some of the code may be reusable (And of a higher quality than other old projects), I've deemed it not worthy of keeping this repo active. Feel free to fork and use the code however you want. No need to ask for permission!

---

ArchT5 is a Java based core which developers can easily build their Discord bots around quickly without having to worry about backend management. It's highly modifiable while still being secure so it's friendly to both developers which want a powerful yet simple API and users which want to ensure the plugins they're using are safe and will not destroy their server.

The ArchT5 structure is based off the [JDA REST API](https://github.com/DV8FromTheWorld/JDA) created by [@DV8FromTheWorld](https://github.com/DV8FromTheWorld) and it uses the current latest release. Literally anything which directly interacts with Discord requires usage of the API so it's recommended you look through their documentation. An interface layer is being worked on which should make certain tasks easier/cleaner for developers but that's not a priority due to everything being possible already. You may wonder after reading this "So why is ArchT5 here?" - We're here just to make some parts of development easier cause who wants to write a command system each time. Plus, mixing and matching of plugins becomes readily available so you don't need to write a components system yourself! That's why we're here.

ArchT5 uses plugins in order to make enabling and disabling different functions real-time easy without having to restart your bot, making it quick and eliminating downtime. Plugins also have security restrictions which stop them from misbehaving and hijacking your bot and they are still being improved to ensure your bot is safe. If you spot a flaw whilst reviewing our source code or a plugin triggers a security safeguard, **please report it on our issues section.** A few improvements we plan to make are:

- The ability to hide your main config (Protecting those tokens <o/ )
- Disabling plugins which hit ratelimits constantly
- Disabling plugins which kick/ban tons of users at once (Raid-proofTM)
- SecurityCenter for watching all processes and analysing suspicious activity

All of the above will be able to be disabled just incase you have a genuine use for hitting rate limits constantly or kicking hundreds of people. If for some reason you want to bypass all plugin security/general security, we recommend you modify the source code. The master branch is a good one to fork as it's usually only pushed to when complete.

---



## Setup (For Discord Usage)

 - Download the Jar of whichever version you want. The Latest version or the Latest noplugin is recommended. 
 - Then create a file called "bot.cfgp" in the same file as the Jar. Open this in a text editor.
  - In the editor, copy and paste this.

```
// Main Configuration File
[string]token=TOKEN_HERE
[string]prefix=!
```

  - Replace "TOKEN_HERE" with your bot token? Don't know what that is? Look up a tutorial on how to create a discord bot account.
  - Replace "!" with whatever you want your command's prefix to be. Want / commands? Replace it with a "/" 
 - Then launch the Java program in whatever way you want, just launch it

 - If the bot doesn't seem to launch, look for the logs

## Setup (For Plugins)

Plugins aren't ready just yet so you'll have to modify the source for now and port your changes over once they're finished.

---



## Technologies

### ConfigPlus:
ConfigPlus is our system for reading config files. It makes storing different types of data easy and human readable while keeping a relativley traditional layout with comments using "//" and entries following a key=value style. Here's an example of a file:

```
config.cfgp

//Woah!
[string]token=ABcD1234
[int]security-level=7

//Comments in the middle?
[float]random-number=2
```

Supported types include:
 - string
 - int (Can also be "integer")
 - uint (Unsigned int)
 - array (Value should have items seperated by ",")
 - map (Value is like an array but each item should have a key and a value seperated by a ":")
 - json (Not quite supported)

### TextDB
Unfinished. Will be documented fully on the plugin release. It's a "replacement" for regular databases, for example if a database goes offline and you need a backup. It's also useful for smaller Databases.


### And a few more which are generally self explanatory like:
 - CommandManager (A simple command manager with help, management, and listing)
 - Multichannel Event Loop (Events which can target channels which listeners can ignore)
 - Logging (A fancy logging system cause why not.)
 - Behaviors (Reprogrammable parts of the system which plugins can fully replace)
  - Behaviors are slowly being added as bot components get finalized so there aren't many.
  - We hope behaviors are adopted by plugins to make systems better.
  - One of the current reprogrammable parts is the command dispatcher. It provides a raw command (Message For)
