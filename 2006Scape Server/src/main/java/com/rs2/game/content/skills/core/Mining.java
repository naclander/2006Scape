package com.rs2.game.content.skills.core;

import com.rs2.event.*;
import com.rs2.game.items.ItemAssistant;
import com.rs2.game.objects.Object;
import com.rs2.game.players.Player;
import com.rs2.game.players.PlayerHandler;
import com.rs2.util.Misc;
import com.rs2.world.clip.Region;

public class Mining {
	
	/**
	 * Andrew (Mr Extremez)
	 */
	private static final int[] RANDOM_GEMS = {1617, 1619, 1621, 1623};
	private static final int[] GLORIES = {1706, 1708, 1710, 1712};
	
	public boolean giveGem(Player player) {
		int chance = 256;
		for (int i = 0; i < GLORIES.length; i++) {
			if (player.playerEquipment[player.playerAmulet] == GLORIES[i]) {
				chance = 86;
			}
		}
		return Misc.random(chance) == 1;
	}

	public void obtainGem(Player player) {
		int reward = RANDOM_GEMS[(int)(RANDOM_GEMS.length * Math.random())];
		player.getItemAssistant().addItem(reward, 1);
		player.getPacketSender().sendMessage("You found an " + ItemAssistant.getItemName(reward) + ".");
	}
	
	public final int[][] Pick_Settings = {
		{1265, 1, 1, 625}, //Bronze
		{1267, 1, 2, 626}, //Iron
		{1269, 6, 3, 627}, //Steel
		{1273, 21, 4, 629}, //Mithril
		{1271, 31, 5, 628}, //Addy
		{1275, 41, 6, 624}, //Rune
	};

	public static enum gems {
		OPAL(1625, 60),
		JADE(1627, 30),
		RED_TOPAZ(1629, 15),
		SAPHIRE(1623, 9),
		EMERALD(1621, 5),
		RUBY(1619, 5),
		DIAMOND(1617, 4);

		public final int itemID;
		public final int chance;

		gems(int itemID, int chance){
			this.itemID = itemID;
			this.chance = chance;
		}


		public static int getRandom(){
			final int maxChance = 128;
			int random = (int) Math.floor(Math.random() * maxChance);
			int index = 0;
			for (gems gem: gems.values()){
				index += gem.chance;
				if (index >= random)
					return gem.itemID;
			}
			return gems.OPAL.itemID;
		}
	}

	public static enum rockData {
		ESSENCE(new int[] { 2491 }, 1, 5, 2, 0, new int[] { 1436, 7936 }),
		CLAY(new int[] { 2108, 2109, 11189, 11190, 11191, 9713, 9711, 14905, 14904 }, 1, 5, 1, 2, new int[] { 434 }),
		COPPER(new int[] { 3042, 2091, 2090, 9708, 9709, 9710, 11960, 14906, 14907 }, 1, 18, 1, 4, new int[] { 436 }),
		TIN(new int[] { 2094, 2095, 3043, 9716, 9714, 11958, 11957, 11959, 11933, 11934, 11935, 14903, 14902 }, 1, 18, 1, 4, new int[] { 438 }),
		BLURITE(new int[] { 10574, 10583, 10584, 2110 }, 10, 20, 1, 42, new int[] { 668 }),
		IRON(new int[] { 450, 2093, 2092, 9717, 9718, 9719, 11962, 11956, 11954, 14856, 14857, 14858, 14914, 14913 }, 15, 35, 2, 9, new int[] { 440 }),
		SILVER(new int[] { 2101, 11186, 11187, 11188, 2100 }, 20, 40, 3, 100, new int[] { 442 }),
		COAL(new int[] { 2096, 2097, 11963, 11964, 14850, 14851, 14852, 11930, 11931, 11932 }, 30, 50, 4, 50, new int[] { 453 }),
		GOLD(new int[] { 2099, 2098, 11183, 11184, 11185, 9720, 9722 }, 40,	65, 6, 100, new int[] { 444 }),
		MITHRIL(new int[] { 2103, 2102, 14853, 14854, 14855 }, 55, 80, 8, 200, new int[] { 447 }),
		ADAMANT(new int[] { 2104, 2105, 14862, 14863, 14864 }, 70, 95, 10, 400, new int[] { 449 }),
		RUNE(new int[] { 14859, 14860, 2106, 2107 }, 85, 125, 20, 1200, new int[] { 451 }),
		GRANITE(new int[] { 10947 }, 45, 75, 10, 8, new int[] { 6979, 6981, 6983 }),
		SANDSTONE(new int[] { 10946 }, 35, 60, 5, 8, new int[] { 6971, 6973, 6975, 6977 }),
		GEM(new int[] {2111}, 40, 65, 6, 175, new int[] {});

		private final int levelReq, mineTimer, respawnTimer, xp;
		private final int[] oreIds;
		private final int[] objectId;

		private rockData(final int[] objectId, final int levelReq, final int xp, final int mineTimer, final int respawnTimer, final int... oreIds) {
			this.objectId = objectId;
			this.levelReq = levelReq;
			this.xp = xp;
			this.mineTimer = mineTimer;
			this.respawnTimer = respawnTimer;
			this.oreIds = oreIds;
		}

		public int getObject(final int object) {
			for (int element : objectId) {
				if (object == element) {
					return element;
				}
			}
			return -1;
		}

		public static rockData getRock(final int object) {
			for (final rockData rock : rockData.values()) {
				if (object == rock.getObject(object)) {
					return rock;
				}
			}
			return null;
		}

		public int getRequiredLevel() {
			return levelReq;
		}

		public int getXp() {
			return xp;
		}

		public int getTimer() {
			return mineTimer;
		}

		public int getRespawnTimer() {
			return respawnTimer;
		}

		public int[] getOreIds() {
			return oreIds;
		}

		public int getOre(int playerLevel){
			if (this == rockData.ESSENCE)
				return playerLevel < 30 ? oreIds[0] : oreIds[1];
			if (this == rockData.GEM)
				return gems.getRandom();

			// return a random ore from the possibilities
			return oreIds[(int) Math.floor(Math.random() * oreIds.length)];
		}
	}
	
	public void repeatAnimation(final Player c) {
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (c.isMining) {
					c.startAnimation(Pick_Settings[c.miningAxe][3]);
					c.getPacketSender().sendSound(432, 100, 0);
				} else {
					container.stop();
				}
			}

			@Override
			public void stop() {
				c.startAnimation(65535);
				c.isMining = false;
			}
		}, 3);
	}
	
	public void startMining(final Player player, final int objectID, final int objectX, final int objectY, final int type) {
		CycleEventHandler.getSingleton().stopEvents(player, "miningEvent".hashCode());
		if (player.isMining || player.miningRock) 
			return;
		int miningLevel = player.playerLevel[player.playerMining];
		rockData rock = rockData.getRock(objectID);
		player.miningAxe = -1;
		player.turnPlayerTo(objectX, objectY);
		// check if the player has required level for this rock
		if (rock.getRequiredLevel() > miningLevel) {
			player.getPacketSender().sendMessage("You need a Mining level of " + rock.getRequiredLevel() + " to mine this rock.");
			return;
		}
		// check id the player has a pickaxe they can use on them
		for (int i = 0; i < Pick_Settings.length; i++) {
			if (player.getItemAssistant().playerHasItem(Pick_Settings[i][0]) || player.playerEquipment[player.playerWeapon] == Pick_Settings[i][0]) {
				if (Pick_Settings[i][1] <= miningLevel) {
					player.miningAxe = i;
				}
			}
		}
		if (player.miningAxe == -1) {
			player.getPacketSender().sendMessage("You need a pickaxe to mine this rock.");
			return;
		}
		if (player.getItemAssistant().freeSlots() < 1) {
			player.getPacketSender().sendMessage("You do not have enough inventory slots to do that.");
			return;
		}

		player.startAnimation(Pick_Settings[player.miningAxe][3]);
		player.getPacketSender().sendSound(432, 100, 0);
		player.isMining = true;
		repeatAnimation(player);
		player.rockX = objectX;
		player.rockY = objectY;
		player.miningRock = true;

		// Tutorial only stuff
		if (player.tutorialProgress == 17 || player.tutorialProgress == 18) {
			player.getPacketSender().chatbox(6180);
			player.getDialogueHandler().chatboxText("", "Your character is now attempting to mine the rock.", "This should only take a few seconds.", "", "Please wait");
			player.getPacketSender().chatbox(6179);
		} else {
			player.getPacketSender().sendMessage("You swing your pick at the rock.");
		}

		CycleEventHandler.getSingleton().addEvent("miningEvent".hashCode(), player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				int oreID = rock.getOre(miningLevel);
				if (!player.isMining) {
					container.stop();
					player.startAnimation(65535);
					return;
				}
				if (player.isMining) {
					if (!giveGem(player)) {
						player.getItemAssistant().addItem(oreID, 1);
						player.getPlayerAssistant().addSkillXP(rock.getXp(), player.playerMining);
						player.getPacketSender().sendMessage("You manage to mine some " + ItemAssistant.getItemName(oreID).toLowerCase() + ".");
					} else {
						obtainGem(player);
					}
				}
				if (player.tutorialProgress == 17) {
					if (rock != rockData.TIN) {
						player.getDialogueHandler().sendStatement("You should mine tin first.");
						resetMining(player);
						return;
					}
					if (player.getItemAssistant().playerHasItem(438)) {
						player.getPacketSender().createArrow(3086, 9501, player.getH(), 2);
						player.getDialogueHandler().chatboxText("Now you have some tin ore you must need some copper ore, then", "you'll have all you need to create a bronze bar. As you did before", "riger click on the copper rock and select 'mine'.", "", "Mining");
						player.tutorialProgress = 18;
					}
				} else if (player.tutorialProgress == 18) {
					if (rock != rockData.COPPER) {
						player.getDialogueHandler().sendStatement("You have already mined this type of ore, now try the other.");
						resetMining(player);
						return;
					}
					if (player.getItemAssistant().playerHasItem(436)) {
						player.getPacketSender().createArrow(3078, 9495, 0, 2);
						player.getDialogueHandler().sendDialogues(3061, -1);
					}
				}
				if (player.getItemAssistant().freeSlots() < 1) {
					player.getPacketSender().sendMessage("You have ran out of inventory slots.");
					container.stop();
				}
				mineRock(rock.getRespawnTimer(), objectX, objectY, type, objectID);
				container.stop();
				if (rock == rockData.ESSENCE) {
					startMining(player, objectID, objectX, objectY, type);
				}
			}
			@Override
			public void stop() {
				player.getPacketSender().closeAllWindows();
				player.startAnimation(65535);
				player.isMining = false;
				player.rockX = 0;
				player.rockY = 0;
				player.miningRock = false;
				return;
			}
		}, getTimer(rock, player.miningAxe, miningLevel));
	}
	
	public static void resetMining(Player player) {
		player.getPacketSender().closeAllWindows();
		player.startAnimation(65535);
		player.isMining = false;
		player.rockX = 0;
		player.rockY = 0;
		player.miningRock = false;
	}

	public int getTimer(rockData rock, int pick, int level) {
		double timer = (int)((rock.getRequiredLevel() * 2) + 20 + Misc.random(20))-((Pick_Settings[pick][2] * (Pick_Settings[pick][2] * 0.75)) + level);
		if (timer < 2.0) {
			return 2;
		} else {
			return (int)timer;
		}
	}

	public void mineRock(int respawnTime, int x, int y, int type, int i) {
		if (i != 2491) {
			new Object(452, x, y, 0, type, 10, i, respawnTime);
			Region.addObject(452, x, y, 0, 10, type, false);

			for (int t = 0; t < PlayerHandler.players.length; t++) {
				if (PlayerHandler.players[t] != null) {
					if (PlayerHandler.players[t].rockX == x && PlayerHandler.players[t].rockY == y) {
						PlayerHandler.players[t].isMining = false;
						PlayerHandler.players[t].startAnimation(65535);
						PlayerHandler.players[t].rockX = 0;
						PlayerHandler.players[t].rockY = 0;
					}
				}
			}
		}
	}
	
	public static void prospectRock(final Player player, final String itemName) {
		if (player.tutorialProgress == 15 || player.tutorialProgress == 16) {
			player.getPacketSender().closeAllWindows();
			player.getPacketSender().chatbox(6180);
			player.getDialogueHandler()
			.chatboxText(
					"Please wait.",
					"Your character is now attempting to prospect the rock. This should",
					"only take a few seconds.", "", "");
			player.getPacketSender().chatbox(6179);
			   CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
		            @Override
		            public void execute(CycleEventContainer container) {
					if (player.tutorialProgress == 15) {
						player.getPacketSender().sendMessage("This rock contains " + itemName.toLowerCase() + ".");
						player.getPacketSender().chatbox(6180);
						player.getDialogueHandler()
						.chatboxText(
								"",
								"So now you know there's tin in the grey rocks. Try prospecting",
								"the brown ones next.", "",
								"It's tin");
						player.getPacketSender().createArrow(3086, 9501,
								player.getH(), 2);
						player.getPacketSender().chatbox(6179);
						player.tutorialProgress = 16;
						container.stop();
						return;
					} else if (player.tutorialProgress == 16) {
						player.getPacketSender().sendMessage("This rock contains " + itemName.toLowerCase() + ".");
						player.getPacketSender().chatbox(6180);
						player.getDialogueHandler()
						.chatboxText(
								"Talk to the Mining Instructor to find out about these types of",
								"ore and how you can mine them. He'll even give you the",
								"required tools.", "",
								"It's copper");
						player.getPacketSender().createArrow(1, 5);
						player.getPacketSender().chatbox(6179);
						container.stop();
						return;
					}
					player.getPacketSender().sendMessage("This rock contains " + itemName.toLowerCase() + ".");
					container.stop();
				}

					@Override
					public void stop() {
						
					}

			}, 3);
			return;
		}
		player.getPacketSender().sendMessage("You examine the rock for ores...");
		   CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
	            @Override
	            public void execute(CycleEventContainer container) {
				player.getPacketSender().sendMessage("This rock contains " + itemName + ".");
				container.stop();
			}
			@Override
				public void stop() {
					
				}
		}, 3);
	}

	public static void prospectNothing(final Player c) {
		c.getPacketSender().sendMessage("You examine the rock for ores...");
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
	          	@Override
	            public void execute(CycleEventContainer container) {
				c.getPacketSender().sendMessage("There is no ore left in this rock.");
				container.stop();
			}
			@Override
				public void stop() {
					
				}
		}, 2);
	}

	public static boolean rockExists(int rockID) {
		for (final rockData rock : rockData.values()) {
			if (rockID == rock.getObject(rockID)) {
				return true;
			}
		}
		return false;
	}

}
