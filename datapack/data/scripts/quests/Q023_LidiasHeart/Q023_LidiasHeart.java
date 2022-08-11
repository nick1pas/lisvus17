/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package quests.Q023_LidiasHeart;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;
import net.sf.l2j.gameserver.network.serverpackets.NpcSay;

public class Q023_LidiasHeart extends Quest
{
	// NPCs
	private static final int INNOCENTIN = 8328;
	private static final int BROKEN_BOOKSHELF = 8526;
	private static final int GHOST_OF_VON_HELLMANN = 8524;
	private static final int TOMBSTONE = 8523;
	private static final int VIOLET = 8386;
	private static final int BOX = 8530;
	
	// NPC instance
	private L2NpcInstance _ghost = null;
	
	// Items
	private static final int FOREST_OF_DEADMAN_MAP = 7063;
	private static final int SILVER_KEY = 7149;
	private static final int LIDIA_HAIRPIN = 7148;
	private static final int LIDIA_DIARY = 7064;
	private static final int SILVER_SPEAR = 7150;
	
	public static void main(String[] args)
	{
		new Q023_LidiasHeart();
	}

	public Q023_LidiasHeart()
	{
		super(23, Q023_LidiasHeart.class.getSimpleName(), "Lidia's Heart");
		
		setItemsIds(SILVER_KEY, LIDIA_DIARY, SILVER_SPEAR);
		
		addStartNpc(INNOCENTIN);
		addTalkId(INNOCENTIN, BROKEN_BOOKSHELF, GHOST_OF_VON_HELLMANN, VIOLET, BOX, TOMBSTONE);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("8328-02.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
			st.giveItems(FOREST_OF_DEADMAN_MAP, 1);
			st.giveItems(SILVER_KEY, 1);
		}
		else if (event.equalsIgnoreCase("8328-06.htm"))
		{
			st.set("cond", "2");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("8526-05.htm"))
		{
			if (!st.hasQuestItems(LIDIA_HAIRPIN))
			{
				st.giveItems(LIDIA_HAIRPIN, 1);
				if (st.hasQuestItems(LIDIA_DIARY))
				{
					st.set("cond", "4");
					st.playSound(QuestState.SOUND_MIDDLE);
				}
				else
					st.playSound(QuestState.SOUND_ITEMGET);
			}
		}
		else if (event.equalsIgnoreCase("8526-11.htm"))
		{
			if (!st.hasQuestItems(LIDIA_DIARY))
			{
				st.giveItems(LIDIA_DIARY, 1);
				if (st.hasQuestItems(LIDIA_HAIRPIN))
				{
					st.set("cond", "4");
					st.playSound(QuestState.SOUND_MIDDLE);
				}
				else
					st.playSound(QuestState.SOUND_ITEMGET);
			}
		}
		else if (event.equalsIgnoreCase("8328-11.htm"))
		{
			if (st.getInt("cond") < 5)
			{
				st.set("cond", "5");
				st.playSound(QuestState.SOUND_MIDDLE);
			}
		}
		else if (event.equalsIgnoreCase("8328-19.htm"))
		{
			st.set("cond", "6");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("8524-04.htm"))
		{
			st.set("cond", "7");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(LIDIA_DIARY, 1);
		}
		else if (event.equalsIgnoreCase("8523-02.htm"))
		{
			if (_ghost == null)
			{
				_ghost = addSpawn(8524, 51432, -54570, -3136, 0, false, 60000);
				_ghost.broadcastPacket(new NpcSay(_ghost.getObjectId(), 0, _ghost.getNpcId(), "Who awoke me?"));
				startQuestTimer("ghost_cleanup", 58000, null, player, false);
			}
		}
		else if (event.equalsIgnoreCase("8523-05.htm"))
		{
			// Don't launch twice the same task...
			if (getQuestTimer("tomb_digger", null, player) == null)
				startQuestTimer("tomb_digger", 10000, null, player, false);
		}
		else if (event.equalsIgnoreCase("tomb_digger"))
		{
			htmltext = "8523-06.htm";
			st.set("cond", "8");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.giveItems(SILVER_KEY, 1);
		}
		else if (event.equalsIgnoreCase("8530-02.htm"))
		{
			st.set("cond", "10");
			st.playSound(QuestState.SOUND_MIDDLE);
			st.takeItems(SILVER_KEY, 1);
			st.giveItems(SILVER_SPEAR, 1);
		}
		else if (event.equalsIgnoreCase("ghost_cleanup"))
		{
			_ghost = null;
			return null;
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg();
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case State.CREATED:
				QuestState st2 = player.getQuestState("Q022_TragedyInVonHellmannForest");
				if (st2 != null && st2.isCompleted())
				{
					if (player.getLevel() >= 64)
						htmltext = "8328-01.htm";
					else
						htmltext = "8328-00a.htm";
				}
				else
					htmltext = "8328-00.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case INNOCENTIN:
						if (cond == 1)
							htmltext = "8328-03.htm";
						else if (cond == 2)
							htmltext = "8328-07.htm";
						else if (cond == 4)
							htmltext = "8328-08.htm";
						else if (cond == 5)
						{
							if (st.getInt("diary") == 1)
								htmltext = "8328-14.htm";
							else
								htmltext = "8328-11.htm";
						}
						else if (cond > 5)
							htmltext = "8328-21.htm";
						break;
					
					case BROKEN_BOOKSHELF:
						if (cond == 2)
						{
							htmltext = "8526-00.htm";
							st.set("cond", "3");
							st.playSound(QuestState.SOUND_MIDDLE);
						}
						else if (cond == 3)
						{
							if (!st.hasQuestItems(LIDIA_DIARY))
								htmltext = (!st.hasQuestItems(LIDIA_HAIRPIN)) ? "8526-02.htm" : "8526-06.htm";
							else if (!st.hasQuestItems(LIDIA_HAIRPIN))
								htmltext = "8526-12.htm";
						}
						else if (cond > 3)
							htmltext = "8526-13.htm";
						break;
					
					case GHOST_OF_VON_HELLMANN:
						if (cond == 6)
							htmltext = "8524-01.htm";
						else if (cond > 6)
							htmltext = "8524-05.htm";
						break;
					
					case TOMBSTONE:
						if (cond == 6)
							htmltext = (_ghost == null) ? "8523-01.htm" : "8523-03.htm";
						else if (cond == 7)
							htmltext = "8523-04.htm";
						else if (cond > 7)
							htmltext = "8523-06.htm";
						break;
					
					case VIOLET:
						if (cond == 8)
						{
							htmltext = "8386-01.htm";
							st.set("cond", "9");
							st.playSound(QuestState.SOUND_MIDDLE);
						}
						else if (cond == 9)
							htmltext = "8386-02.htm";
						else if (cond == 10)
						{
							if (st.hasQuestItems(SILVER_SPEAR))
							{
								htmltext = "8386-03.htm";
								st.takeItems(SILVER_SPEAR, 1);
								st.rewardItems(57, 100000);
								st.playSound(QuestState.SOUND_FINISH);
								st.exitQuest(false);
							}
							else
							{
								htmltext = "8386-02.htm";
								st.set("cond", "9");
							}
						}
						break;
					
					case BOX:
						if (cond == 9)
							htmltext = "8530-01.htm";
						else if (cond == 10)
							htmltext = "8530-03.htm";
						break;
				}
				break;
			
			case State.COMPLETED:
				if (npc.getNpcId() == VIOLET)
					htmltext = "8386-04.htm";
				else
					htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
}