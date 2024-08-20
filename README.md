# ToT-Initiative

Bukkit initiative roller plugin for the Tales of Toril (www.talesoftoril.com) D&D Minecraft roleplaying system. 


# Commands
/init create — creates an empty initiative tracker

/init insert <creature name> <initiative roll.dexternity score> — inserts a creature into the initiative tracker using their initiative roll followed by their dexterity score (e.g. 20.13 for a roll of 20 with a dexterity score of 13)

/init insert <creature name> position <position number> — inserts a creature into the initiative tracker at a certain position

/init list — lists out the creatures in the initiative tracker to the DM only

/init list announce — announces the initiative tracker to all players in 60 blocks

/init next — moves to the next turn in the initiative order and announces it

/init prev — moves to the previous turn in the initiative order and announces it 

/init end — ends initiative and clears initiative order

/init remove name <creature name> — Removes the first creature matching the name from the initiative tracker.

/init remove position <position> — Removes the creature at the given position from the initiative tracker.

/init query <minecraft username> — Queries the player for their initiative order and displays it for the user to see. This is only seen by the person who sends the command.

/init query <minecraft username> current — Queries the player for the current creature taking their turn. This is only seen by the person who sends the command.
