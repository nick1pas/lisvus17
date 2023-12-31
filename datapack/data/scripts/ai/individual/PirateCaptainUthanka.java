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
package ai.individual;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.NpcSay;

public class PirateCaptainUthanka extends Quest
{
    private static final int PIRATE_CAPTAIN_UTHANKA = 520;
    // NOT retail. I couldn't found what's the original timeout (if any)
    // so this is just a guest (5 minutes)
    private static final long SHOUT_MESSAGE_TIMEOUT_IN_SECONDS = 300000;
    
    public static void main(String[] args)
    {
        new PirateCaptainUthanka();
    }
    
    public PirateCaptainUthanka()
    {
        super(-1, "piratecaptainuthanka", "ai/individual");
        registerNPC(PIRATE_CAPTAIN_UTHANKA);
    }
    
    private void shoutCaptainMessage(L2NpcInstance npc)
    {
        if (npc.isInCombat())
        {
            return;
        }
        NpcSay message = new NpcSay(npc.getObjectId(), Say2.SHOUT, npc.getNpcId(), "Wah, ha, ha, ha! Uthanka has taken over this island today!");
        npc.broadcastPacket(message);
    }
    
    @Override
    public String onSpawn(L2NpcInstance npc)
    {
        shoutCaptainMessage(npc);
        startQuestTimer("shout", SHOUT_MESSAGE_TIMEOUT_IN_SECONDS, npc, null, true);
        return super.onSpawn(npc);
    }
    
    @Override
    public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
    {
        if (event.equalsIgnoreCase("shout"))
        {
            shoutCaptainMessage(npc);
        }
        return super.onAdvEvent(event, npc, player);
    }
    
    @Override
    public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
    {
        cancelQuestTimer("shout", npc, null);
        return super.onKill(npc, killer, isPet);
    }
}
