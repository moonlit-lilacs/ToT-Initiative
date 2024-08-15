package io.github.moonlitlilacs.ToTInitiative;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.joml.Math;


public class InitiativeTracker implements CommandExecutor {

	private final ToTInitiative plugin;
	private final HashMap<Player, LinkedList<Tuple<String, Float>>> trackers;
	
	public InitiativeTracker(ToTInitiative plugin) {
		this.plugin = plugin;
		trackers = new HashMap<Player, LinkedList<Tuple<String, Float>>>();
	}
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;
		
		if (args == null || args.length == 0) {
			return false;
		}
		
		
		if (cmd.getName().equalsIgnoreCase("init")) {
			
			//handle initiative tracker creation
			switch (args[0]) {
				case "create": {
					if (args.length != 1) {
						player.sendMessage("Invalid arg length. Expected 1.");
						return false;
					}
					if (trackers.containsKey(player)){
						player.sendMessage("You currently may not create multiple intiative trackers. Please use `/init end` to end your current initiative tracker.");
						return false;
					}
					player.sendMessage("Toss a coin to your Witcher, o' valley of plenty. Please donate 1 Inspiration Token to Lilac as payment for the use of this initiative tracker.");
					trackers.put(player, new LinkedList<Tuple<String, Float>>());
					player.sendMessage("Initiative tracker created. Please use `/init insert <creature name [no spaces]> initiative <initiative>` or `/init insert <creature name [no spaces]> position <position>` to insert creatures into your initiative tracker.");
					return true;
				}
				case "end": {
					if (args.length != 1) {
						player.sendMessage("Invalid arg length. Expected 1.");
						return false;
						}
					if (!trackers.containsKey(player)) {
						player.sendMessage("You currently do not own a valid initiative tracker. Please use `/init create` to create a new initiative tracker.");
						return false;
					}
				
					else {
						player.sendMessage("Initiative destroyed.");
						trackers.remove(player);
						return true;
					}
				}
					
				case "insert": {
					if (args.length != 4) {
						player.sendMessage("Invalid arg length. Command usage: `/init insert <creature-name> <initiative/position> <initiative-roll.dexterty score / position number>`");
						return false;
					}
					
					LinkedList<Tuple<String, Float>> ll = trackers.get(player);
					
					if (!trackers.containsKey(player)) {
						player.sendMessage("You currently do not own a valid initiative tracker. Please use `/init create` to create a new initiative tracker.");
						return false;
					}
					
					
					String creatureName = args[1];
					if (args[2].equalsIgnoreCase("initiative")) {
						
						try {
							Float init = Float.parseFloat(args[3]);
							Tuple<String, Float> node = new Tuple<String, Float>(creatureName, init);
							
							if (ll.isEmpty()) {
								ll.addFirst(node);
								player.sendMessage("[Init]     " + node.x + " successfully added to intiative tracker.");
								return true;
							}
							
							for (Tuple<String, Float> n : ll) {
								if (n.y == init) {
									if (Math.random() > 0.5) {
										ll.add(ll.indexOf(n), n);
									}
									else {
										ll.add(ll.indexOf(n)+1, n);
									}
								}
								else if (n.y < init) {
									ll.add(ll.indexOf(n), node);
									player.sendMessage("[Init]     " + node.x + " successfully added to intiative tracker.");
									return true;
								}
							}
							
							ll.addLast(node);
							player.sendMessage("[Init]     " + node.x + " successfully added to initiative tracker.");
							return true;
							
						
						} catch (NumberFormatException e) {
							player.sendMessage("Initiative must be passed as a floating point value <initiative-roll.dexterity-score> to prevent premature roll-offs in the event of ties.");
							return false;
						}
						
						
							
					}
					
					else if (args[2].equalsIgnoreCase("position")) {
						try {
							
							
							Integer pos = Integer.parseInt(args[3])-1;
							
							Float fakeInit;
							
							if (pos >= ll.size()) {
								fakeInit = (float) 0.0;
							}
							else {
								fakeInit = (float) (ll.get(pos).y+0.001);
							}
							
							Tuple<String, Float> node = new Tuple<String,Float>(creatureName, fakeInit);
							
							if (ll.isEmpty()) {
								ll.addFirst(node);
								player.sendMessage("[Init]     " + node.x + " successfully added to intiative tracker.");
								return true;
							}
							else {
								ll.add(pos, node);
							}
							
							player.sendMessage("[Init]     " + node.x + " successfully added to intiative tracker.");
							return true;
							
							
						} catch (NumberFormatException e) {
							player.sendMessage("Position must be passed as a 1-indexed integer representing the position in the intiative order with respects to its current ordering.");
							return false;
						}
					}
					
					else {
						player.sendMessage("Invalid command.");
						return false;
					}

				}
				
				case "list": {
					
					if (!trackers.containsKey(player)) {
						player.sendMessage("You currently do not own a valid initiative tracker. Please use `/init create` to create a new initiative tracker.");
						return false;
					}
					
					if (args.length == 1) {

						LinkedList<Tuple<String, Float>> ll = trackers.get(player);
						
						
						
						
						player.sendMessage("Initiative Order");
						
						for (Tuple<String, Float> n : ll) {
							player.sendMessage("[Init]     " + n.x + " - " + String.valueOf(n.y));
						}
						
						return true;
					}
					
					else if (args[1].equalsIgnoreCase("announce")) {
						LinkedList<Tuple<String, Float>> ll = trackers.get(player);
						

						Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
						
						Location playerLocation = player.getLocation();
						for (Player p : players) {
							
							player.sendMessage("[Init]     Initiative Order");
							if (p.getLocation().distance(playerLocation) < 60) {
								for (Tuple<String, Float> n : ll) {
									p.sendMessage("[Init]     " + n.x + " - " + String.valueOf(n.y));
								}
							}
						}
						

						
						return true;
					}
					
					else {
						player.sendMessage("Invalid command.");
						return false;
					}
					
					
					
				}
					
					
				case "next": {
					
					if (!trackers.containsKey(player)) {
						player.sendMessage("You currently do not own a valid initiative tracker. Please use `/init create` to create a new initiative tracker.");
						return false;
					}
					
					LinkedList<Tuple<String, Float>> ll = trackers.get(player);
					
					if (ll.isEmpty()) {
						player.sendMessage("You cannot use `next` on an empty initiative tracker. Please use `/init insert` to add creatures to your initiative tracker.");
						return false;
					}
					
					Tuple<String, Float> removed = ll.pop();
					ll.addLast(removed);
					Tuple<String, Float> current = ll.peek();
					
				
//					player.sendMessage(current.x + " is now taking their turn.");
					Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
					
					Location playerLocation = player.getLocation();

					for (Player p : players) {
						if (p.getLocation().distance(playerLocation) < 60) {
							p.sendMessage("[Init]     " + current.x + " is now taking their turn.");
						}
					}

					
					return true;
				
				}
				
				case "prev" : {
					
					if (!trackers.containsKey(player)) {
						player.sendMessage("You currently do not own a valid initiative tracker. Please use `/init create` to create a new initiative tracker.");
						return false;
					}
					
					LinkedList<Tuple<String, Float>> ll = trackers.get(player);
					
					Tuple<String, Float> last = ll.pollLast();
					
					ll.addFirst(last);
					
					//This code is technically unnecessary, but is useful for debugging so I'm keeping it as a redundancy.
					Tuple<String, Float> current = ll.peek();
					
//					player.sendMessage(current.x + " is now taking their turn.");
					Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
					
					Location playerLocation = player.getLocation();

					for (Player p : players) {
						if (p.getLocation().distance(playerLocation) < 60) {
							p.sendMessage("[Init]      " + current.x + " is now taking their turn.");
						}
					}
				
					
					return true;
					
				}
				
				case "query" : {
					
					if (args.length == 1) {
						player.sendMessage("Invalid argument length");
						return false;
					}
					
					
					if (args.length == 2) {
						
						Player targetPlayer = plugin.getServer().getPlayerExact(args[1]);
						
						if (targetPlayer == null) {
							player.sendMessage("Player is offline or otherwise cannot be found.");
							return false;
						}
						
						if (!trackers.containsKey(targetPlayer)) {
							player.sendMessage("Target player does not own a valid initiative tracker.");
							return false;
						}
						
						LinkedList<Tuple<String, Float>> ll = trackers.get(targetPlayer);
						
						
						
						player.sendMessage("Initiative Order");
						
						for (Tuple<String, Float> n : ll) {
							player.sendMessage("[Init]     " + n.x + " - " + String.valueOf(n.y));
						}
						
						return true;
					}
					else if (args.length == 3 && args[2].equalsIgnoreCase("current")) {
						
						Player targetPlayer = plugin.getServer().getPlayerExact(args[1]);
						
						if (targetPlayer == null) {
							player.sendMessage("Player is offline or otherwise cannot be found.");
							return false;
						}
						
						LinkedList<Tuple<String, Float>> ll = trackers.get(targetPlayer);
						
						player.sendMessage("[Init]     Creature currently taking their turn is: " + ll.peek().x);
						return true;
					}
					
					
					
				}
				
				case "remove" : {
					if (args.length == 3 && args[1].equalsIgnoreCase("name")) {
						
						if (!trackers.containsKey(player)) {
							player.sendMessage("You do not own a valid initiaitve tracker.");
							return false;
						}
						
						LinkedList<Tuple<String, Float>> ll = trackers.get(player);
						
						String name = args[2];
						
						for (Tuple<String,Float> t : ll) {
							if (t.x.equalsIgnoreCase(name)) {
								ll.remove(t);
								player.sendMessage("[Init]    " + t.x + " was removed from the initiative tracker.");
								return true;
							}
						}
						
						player.sendMessage("Cannot find specified creature. Ensure it exactly matches one of the creatures in your initiative tracker.");
						return false;
					}
					
					if (args.length == 3 && args[1].equalsIgnoreCase("position")) {
						if (!trackers.containsKey(player)) {
							player.sendMessage("You do not own a valid initiative tracker.");
							return false;
						}
						
						LinkedList<Tuple<String, Float>> ll = trackers.get(player);
						
						int position = Integer.parseInt(args[2])-1;
						
						if (position > ll.size()) {
							ll.removeLast();
							player.sendMessage("Position specified is larger than the size of the initiative tracker. Removing last...");
							return true;
						}
						
						if (position < 0) {
							player.sendMessage("Position cannot be less than one.");
							return false;
						}
						
						Tuple<String, Float> removed = ll.remove(position);
						player.sendMessage("[Init]    " + removed.x + " was removed from the initiative tracker.");
						return true;
						
						
					}
					
				}
			
				default:
					player.sendMessage("Invalid command. Please try again.");
					return false;
			}
			
			
			
			
			
//			if (args[0].equalsIgnoreCase("create") && args.length == 1) {
//				
//				if (trackers.containsKey(player)){
//					player.sendMessage("You currently may not create multiple intiative trackers. Please use `/init end` to end your current initiative tracker.");
//					return false;
//				}
//				
//				trackers.put(player, new LinkedList<Tuple<Player, Float>>());
//				player.sendMessage("Initiative tracker created. Please use `/init insert <creature name [no spaces]> initiative <initiative>` or `/init insert <creature name> index <index>` to insert creatures into your initiative tracker.");
//
//				return true;
//			}
			
			//handle initiative tracker destruction
//			if (args[0].equalsIgnoreCase("end") && args.length == 1) {
//				
//				if (!trackers.containsKey(player)) {
//					player.sendMessage("You currently do not own a valid initiative tracker. Please use `/init create` to create a new initiative tracker.");
//					return false;
//				}
//			
//				else {
//					trackers.remove(player);
//				}
//				
//			}
//			
//			if (args[0].equalsIgnoreCase("insert") && args.length == 4) {
//				String creatureName = args[1];
//				 
//				
//				
//			}
			
			
			
			
			
			//if none of our sub-commands match, return false.
				
		}
		
		//if the cmd isn't init, then we can simply return.
		else {
			return false;
		}
		
	}
	
	
}
