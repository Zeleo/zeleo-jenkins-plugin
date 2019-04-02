# Zeleo Jenkins Plugin

## Overview
This plugin allows you to react to build events using the [Zeleo](https://www.zeleo.io) workflow automation and rule engine. What can you do with this? Well- [Zeleo](https://www.zeleo.io) is intentionally a very open platform, so almost anything you want! For example, you could assign tasks to developers on a buid failure to fix tests, trigger unit/integration tests depending on the day of the week to spread out testing, let you Slack channel know every detail of your build, etc. You could even use us to trigger deployments on other systems (like AWS or Azure).

## Jenkins Setup
Getting Jenkins ready to interact with Zeleo is as simple as installing a plugin and adding it to your build job. Follow the steps below to get this working.

1. Add this plugin to your Jenkins installation from the [Jenkins Plugin Site](https://plugins.jenkins.io/). 
2. Configure your build job (or create a new one) and select `Zeleo Build Event` from your _Post Build Events_ drop down.
3. Once added, you will be able to select whether to trigger the event at the start of the build, when the build completes, or both.

![Jenkins Plugin COnfiguration](https://raw.githubusercontent.com/Zeleo/zeleo-jenkins-plugin/master/.images/jenkins-plugin-config.png)

4. Then you need to add the email address of the Zeleo user you want to listen for these build events. Remember, Zeleo needs all events to be tied to a user, even if it's just system to system. This allows Zeleo to engage its social graph of your team if you so desire.
5. That's it! Zeleo will now receive events from your build system from your Jenkins installation. There are some steps on the Zeleo side to get things working, but we'll do that in the next section.

> This does not have to be the email you used for your Zeleo user; you will just need to specify this email address in at least one user's configuration. We did this to allow your team to have more than one build listener if you prefer just by adding this email address to their configuration. We'll get into this more later. And yeah- it doesn't really have to be an email address, you just want a unique string that matches. Email is an easy way to have a unique identifier.

## Zeleo Configuration
Once you have the plugin installed and added to your build job, you need to have Zeleo react to them. There are three simple steps you will need to do to get Zeleo ready.

### Add Jenkins Application to your Team

![Add Jenkins to your Team](https://raw.githubusercontent.com/Zeleo/zeleo-jenkins-plugin/master/.images/zeleo-jenkins-manage-team-dialog.png)

1. The first action you will need to take is to open the _Team Management_ dialog as a team administrator.
2. Click on the _Applications_ tab in this dialog, and find the _Jenkins_ Zeleo Application at the bottom of the dialog.
3. Click the _+_ sign to the right of the _Jenkins_ application. The application will move from _Available Applications_ to _Installed Applications_. 
4. All set! Jenkins is installed to your team.

### Set Up Listener User(s)
Every Zeleo event must have a user that is the target. In this case you will specify this by adding the email address from your _Jenkins Zeleo Plugin_ configuration to one or more users.

1. Log in as the user you want to listen to the buid events.
2. Open the _Edit Profile_ dialog.
3. Select the _Applications_ tab and find _Jenkins_.

![Enter Jenkins Email](https://raw.githubusercontent.com/Zeleo/zeleo-jenkins-plugin/master/.images/zeleo-jenkins-email.png)

4. In the _Jenkins Email Address_ enter the email string you added in `Jenkins Setup` step 4. This really just needs to be the same unique string, case sensitive, but we recommend an email address. You could also generate a UUID if you prefer.

### Create Rules
Now you can do the fun part- configure Zeleo to react to your Jenkins Build Jobs!

> Rules in Zeleo are simple condition statements. There are all sorts of thing you can do with this; Zeleo is a platform to allow you to do whatever you want with few limitations. Please go [here](https://zeleo.github.io/zeleo/) for more information on how to use the platform.

1. Open the left side panel (you must have creator rights- talk to your team administrator if you do not have them) and select _Rules & Conditions_.
2. Click the _+_ button at the bottom left of the screen to create a new rule.
3. Name and describe your new rule for future bookkeeping. 
4. Select Jenkins from the initial dropdown.

![Jenkins Plugin Event](https://raw.githubusercontent.com/Zeleo/zeleo-jenkins-plugin/master/.images/zeleo-jenkins-initial-rule.png)

5. Choose the _Build Event_ Jenkins Event.

![Jenkins Build Event](https://raw.githubusercontent.com/Zeleo/zeleo-jenkins-plugin/master/.images/zeleo-jenkins-build-trigger.png)

6. Set the target of your event. Remember when we added the email address to a user above? Set the _Listener_ variable to _Person_ and the target of the build event will be referenced as _Person_ in the Consequence tab. If you are unsure, just make it look like the screenshot below, as this is the most common configuration.
> This can be a bit confusing, but it is also quite powerful. It allows you to set up a relationship graph among your team and refernce team members by how they relate. For instance, if one or more people were _Direct Reports_ of the _Person_, you can set a consequence to send a Slack Direct Message to all these people. You can read more [here](https://medium.com/zeleo/a-web-of-relationships-leveraging-the-zeleo-social-graph-5960f6fb8657), or just contact us at support@zeleoinc.com and we'll help you out!

![Person Variable](https://raw.githubusercontent.com/Zeleo/zeleo-jenkins-plugin/master/.images/zeleo-jenkins-person-rule.png)

7. Set any other conditions that you want to be `True` in order for the consequence to trigger.

![Example Conditions](https://raw.githubusercontent.com/Zeleo/zeleo-jenkins-plugin/master/.images/zeleo-jenkins-rule-success.png)

8. Click on the Consequence tab and select how you want Zeleo to react! There are all sorts of possibilities here, from sending an email, text message, assigning a Zeleo task, Slack posts (both direct and to a channel), etc. Let us know if what you need isn't listed- we're happy to build out the possibilities of Zeleo!

![Consequence](https://raw.githubusercontent.com/Zeleo/zeleo-jenkins-plugin/master/.images/zeleo-jenkins-consequence.png)

## Conclusion
Zeleo is intentionally powerful at the expense of simplicity. A bold move in this day and age, but we would love to help you to make sure you are getting the most of the Zeleo setup. Please don't hesitate to [reach out](mailto:support@zeleoinc.com)- no charge; we'll make sure you are happy with the product!

Thanks for trying out Zeleo.


