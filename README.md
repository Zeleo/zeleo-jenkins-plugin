# Zeleo Jenkins Plugin

## Overview
This plugin allows you to react to build events using the [Zeleo](https://www.zeleo.io) workflow automation and rule engine. What can you do with this? Well- [Zeleo](https://www.zeleo.io) is intentionally a very open platform, so almost anything you want, but for example, you could assign tasks to developers on a buid failure to fix tests, trigger unit/integration tests depending on the day of the week to spread out testing, let you Slack channel know every detail of your build, etc. You could even use us to trigger deployments on other systems (like AWS or Azure).

## Jenkins Setup
1. Add this plugin to your Jenkins installation from the [Jenkins Plugin Site](https://plugins.jenkins.io/). 
2. Configure yout build job (or create a new one) and select `Zeleo Build Event` from your _Post Build Events_ drop down.
3. Once added, you will be able to select whether to trigger the event at the start of the build, or just when the build completes.
4. Then you need to add the email address of the Zeleo user you want to listen for these build events. Remember, Zeleo needs all events to be tied to a user, even if it's just system to system. This allows Zeleo to engage its social graph of your team if you so desire.
4. That's it! Zeleo will now receive events from your build system from your Jenkins installation. There are some steps on the Zeleo side to get things working, but we'll do that in the next section.

## Zeleo Configuration
Once you have the plugin installed and added to your build job, you need to have Zeleo react to them.
1. The first action you will need to take is top open the _Team Management_ dialog as an administrator and in the _Applications_ tab, add the Jenkins Zeleo App.
2. The second thing you will need to do is set which Zeleo user will be listening to these events. If the _User Profile_ for this user, add the email address you used in Jenkins (step #4 above), including capitalization. All users that match this will be the `Person` in the event.
3. Now you can open the left hand panel and create rules to react to Jenkins events! 
