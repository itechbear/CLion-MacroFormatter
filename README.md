CLion-MacroFormatter
====================

A CLion plugin that shows formatted macro expansion in the code documentation panel

Requirements
============

In order to use this plugin, you should install clang-format. Usually, you just need to install clang.

Installation
==========

 - [jetbrains plugin repository](https://plugins.jetbrains.com/plugin/7674)

Usage
=====

 - Install this plugin
 - Go to "Settings -> Other Settings -> Macro Formatter", set the path of clang-format.
 - Put your cursor on a C/C++ macro
 - Use View | Quick Documentation or the corresponding keyboard shortcut (by default: Ctrl+Q on Windows/Linux and F1 on Mac) with the cursor focused on a macro.

Screenshots
===========

Before :(
![alt life before CLion-MacroFormatter](https://raw.github.com/itechbear/CLion-MacroFormatter/master/screenshots/life-before-this-plugin.png)

After :)
![alt life after CLion-MacroFormatter](https://raw.github.com/itechbear/CLion-MacroFormatter/master/screenshots/life-after-this-plugin.png)
