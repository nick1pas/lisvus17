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
package net.sf.l2j.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.L2ClanMember;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class ...
 * @version $Revision: 1.3.2.1.2.4 $ $Date: 2005/03/27 15:29:39 $
 */
public class PledgeShowMemberListUpdate extends L2GameServerPacket
{
	private static final String _S__69_PLEDGESHOWMEMBERLISTUPDATE = "[S] 54 PledgeShowMemberListAdd";

	private final String _name;
	private final int _level;
	private final int _classId;
	private final int _isOnline;
	private final int _race;
	private final int _sex;
	
	public PledgeShowMemberListUpdate(L2PcInstance player)
	{
		_name = player.getName();
		_level = player.getLevel();
		_classId = player.getClassId().getId();
		_race = player.getRace().ordinal();
		_sex = player.getAppearance().getSex() ? 1 : 0;
		_isOnline = (player.isOnline() && !player.inOfflineMode()) ? player.getObjectId() : 0;
	}

	public PledgeShowMemberListUpdate(L2ClanMember member)
	{
	    _name = member.getName();
	    _level = member.getLevel();
	    _classId = member.getClassId();
	    _isOnline = member.isOnline() ? member.getObjectId() : 0;
		_sex = member.getSex() ? 1 : 0;
	    _race = member.getRaceOrdinal();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x54);
		writeS(_name);
		writeD(_level);
		writeD(_classId);
		writeD(_sex);
		writeD(_race);
		writeD(_isOnline); // 1=online 0=offline
		writeD(0);
		writeD(0);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__69_PLEDGESHOWMEMBERLISTUPDATE;
	}
}