# Hacked Version of Daniel Husons MALT - MEGAN alignment tool

This only exists as we sometimes needed extra functionalities that were not available in MALT by default. For most users it is hihgly advisable to go with Daniels original that is version of MALT which you can find here https://github.com/danielhuson/malt. No really you should as often as possible use Daniels version as your default version. But you can use cMALT as a secondary version if you need any of the hacked functions. Please keep in mind that some if these functions are established by intentionally breaking part of MALT's source code. So in case you encounter any problems with cMALT, you can try to get in contact with me and I will try to fix if and when I get to it. **You shouldn't complain to Daniel Huson about any broken cMAlt functionality**.

But in case you need some of the hacked funtions find a list here:

-dlca --disableLCA Turn off LCA algorithm and force read assignment to leaves. Will increase file size and read count and run time

-u, --customScoring provide a custom scoring Matrix

-r, --removeDup Remove exact sequence Duplicates if when query cashing

-c, --minComp Set a minimum value for query complexity

-asm, --ancientScoringMatrix a use a scoring matrix more lenient towards C>T and G>A transisitons

-cmp, --customMatrixPercent the value here will be used to generate the scoring matrix and reduce the panility for transtions

-tails, --tails  The X bases from each tail of the read will tolerate C>T and G>A substitions reflective of aDNA damage on each tail 
                the percent identity value of those reads will be inflated to allow strict mapping without losing aDNA reads
                
-singleStranded, --singleStranded Set tolarable damage to single stranded pattern, Will change tollerated damage pattern to C>T and C>T for both tails only usefull in conjunction with tails.
