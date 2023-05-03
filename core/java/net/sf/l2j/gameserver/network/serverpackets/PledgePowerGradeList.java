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

import java.util.Arrays;
import java.util.Set;

import net.sf.l2j.gameserver.model.L2ClanMember;

public class PledgePowerGradeList extends L2GameServerPacket
{
    private static final String _S__FE_3B_PLEDGEPOWERGRADELIST = "[S] FE:3B PledgePowerGradeList";

    private final Set<Integer> _ranks;
	private final L2ClanMember[] _members;
	
	public PledgePowerGradeList(Set<Integer> ranks, L2ClanMember[] members)
	{
		_ranks = ranks;
		_members = members;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xFE);
		writeH(0x3b);
		writeD(_ranks.size());
		for (int rank : _ranks)
		{
			writeD(rank);
			writeD((int) Arrays.stream(_members).filter(m -> m.getPowerGrade() == rank).count());
		}
	}

    @Override
    public String getType()
	{
		return _S__FE_3B_PLEDGEPOWERGRADELIST;
	}
}
