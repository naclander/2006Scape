package com.rs2.net.packets.impl;

import com.rs2.GameEngine;
import com.rs2.game.content.skills.firemaking.Firemaking;
import com.rs2.game.players.Player;
import com.rs2.net.packets.PacketType;
import com.rs2.util.Misc;

public class ItemOnGroundItem implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		player.getInStream().readSignedWordBigEndian();
		int itemUsed = player.getInStream().readSignedWordA();
		int groundItem = player.getInStream().readUnsignedWord();
		int gItemY = player.getInStream().readSignedWordA();
		int itemUsedSlot = player.getInStream().readSignedWordBigEndianA();
		int gItemX = player.getInStream().readUnsignedWord();
		if (!player.getItemAssistant().playerHasItem(itemUsed, 1, itemUsedSlot)) {
			return;
		}
		if (!GameEngine.itemHandler.itemExists(groundItem, gItemX, gItemY)) {
			return;
		}

		player.endCurrentTask();

		switch (itemUsed) {
		case 590:
		case 7331:
		case 7330:
		case 7329:
			Firemaking.attemptFire(player, itemUsed, groundItem, gItemX, gItemY, true);
			break;

		default:
			if (player.playerRights == 3) {
				Misc.println("ItemUsed " + itemUsed + " on Ground Item "
						+ groundItem);
			}
			break;
		}
	}

}
