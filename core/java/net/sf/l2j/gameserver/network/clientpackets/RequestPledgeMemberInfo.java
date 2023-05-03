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
package net.sf.l2j.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ClanMember;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.PledgeReceiveMemberInfo;
import net.sf.l2j.gameserver.network.serverpackets.PledgeReceivePowerInfo;

public class RequestPledgeMemberInfo extends L2GameClientPacket
{
    private static final String _C__D0_1D_REQUESTPLEDGEMEMBERINFO = "[C] D0:1D RequestPledgeMemberInfo";

    private String _playerName;
	
	@Override
	protected void readImpl()
	{
		readD();
		_playerName = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance player = getClient().getActiveChar();
		if (player == null)
			return;
		
		final L2Clan clan = player.getClan();
		if (clan == null)
			return;
		
		final L2ClanMember member = clan.getClanMember(_playerName);
		if (member == null)
			return;
		
		player.sendPacket(new PledgeReceiveMemberInfo(member));
	}

    @Override
    public String getType()
    {
        return _C__D0_1D_REQUESTPLEDGEMEMBERINFO;
    }
}
