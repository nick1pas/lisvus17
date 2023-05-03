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

public class RequestPledgeReorganizeMember extends L2GameClientPacket
{
    private static final String _C__D0_24_REQUESTPLEDGEREORGANIZEMEMBER = "[C] D0:24 RequestPledgeReorganizeMember";

    @Override
	protected void readImpl()
	{
		readD();
		readS();
		readD();
		readS();
	}
	
	@Override
	protected void runImpl()
	{
		// Not implemented since subpledges are not supported
	}

    @Override
	public String getType()
	{
		return _C__D0_24_REQUESTPLEDGEREORGANIZEMEMBER;
	}
}
