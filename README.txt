UTEID: crr2494;
FIRSTNAME: Chelsea;
LASTNAME: Rusch;
CSACCOUNT: crusch;
EMAIL: chelsearusch@utexas.edu

[Program 2]
[Description]
This is relatively similar to project 1, with most modifications being in CovertChannel. One difference from 
project 1 is that SecureSystem no longer has to be passed a filename as a constructor argument, but can be fed
instructions straight from the main method, which is closer to how an actual user would work.
All instructions are executed immediately per bit read from the file, and all instructions that have been 
executed are written to log.txt. The program writes the data read from the covert channel to output.txt.
printState() is disabled for brevity's sake.

[Machine Information]
Lenovo desktop with 3.7 GHz CPU, running Windows 10.

[Source Description]
Metamorphosis and Pride and Prejudice obtained from project Gutenberg.
My Immortal is a famously bad Harry Potter fanfiction, and I downloaded
a .doc version from http://myimmortalrehost.webs.com/downloads.htm.'
assignment.txt is the assignment specifications for this project from 
the class website.

[Finish]
I believe every requirement is finished except for the verbose argument. The program ALWAYS creates log.txt 
and will not run with a v argument.

[Results Summary]
[No.]	[DocumentName] 		[Size] 	 	[Bandwidth]
1	Pride and Prejudice	717602 bytes	5.673 bits/ms
2	Metamorphosis		141419 bytes	5.629 bits/ms
3	My Immortal		50157 bytes	5.610 bits/ms
4	assignment		13824 bytes	5.226 bits/ms