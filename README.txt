README
========

/*======================================================================
 * 
 * Pwnee : A lightweight 2D game engine for Java
 * 
 * Copyright (c) 2012 by Stephen Lindberg (sllindberg21@students.tntech.edu)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: 
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
======================================================================*/

Pwnee is a Java-based 2D game engine using Java's AWT and Swing libraries. 

All components and examples of Pwnee unless otherwise specified, have been developed and tested
on a 64-bit computer running Windows 7, but it should also be able to run on any computer 
with at least Java 6 properly installed.



Requirements:
----------------
You'll need the following tools to be able to compile and run Pwnee and its examples:
* At least Java 6 JDK and JRE
* Adobe Ant



Building pwnee.jar from the source code:
-------------------
Open a command terminal in the "development" directory and enter the command
"ant"

This will create the pwnee.jar in the "latest" directory and it will also 
create the Javadocs docs for Pwnee's classes in the "docs" directory.



Building the examples from source:
---------------------
In the root directory for the each example, just open a terminal and enter
"ant all"

The executable jar file for the example will be created in its "latest" directory.



Frequently Asked Questions:
----------------

Q: Why is this game engine called "Pwnee"? 
A: I already named my Javascript + Canvas game engine "Jage", so I couldn't also name 
my Java game engine "Jage". You mad, non-brony?  :)

Q: Is "Pwnee" an acronym for something?
A: Not really, but I guess it could mean something like 
Programming With Nice Electric Equines.

Q: Is this engine currently stable enough to use?
A: Yes it is... mostly. Some parts are in flux, but I'll try not to make many
drastic changes in case somepony else is using Pwnee for their project.

Q: Is there documentation for the Pwnee classes?
A: When you build Pwnee with the ant builder, the Javadocs for Pwnee will be 
generated in the "docs" folder. The classes are also open-source, and I try 
to document/comment them well. 

Q: Do you have an tutorials/examples of how to use Pwnee?
A: Yes, check out the "examples" folder. The "HelloWorld" tutorial will show
you how to set up a very basic application using Pwnee.

Don't see an answer to your question here or in the docs? Fire me an email at
sllindberg21@students.tntech.edu


