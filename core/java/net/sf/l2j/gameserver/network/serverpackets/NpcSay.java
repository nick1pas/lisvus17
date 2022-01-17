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

/**
 * This class ...
 * @version $Revision: 1.4.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class NpcSay extends L2GameServerPacket
{
	private static final String _S__02_CREATURESAY = "[S] 02 NpcSay";
	private final int _objectId;
	private final int _textType;
	private final int _npcId;
	private final String _text;
	
	/**
	 * @param objectId
	 * @param messageType
	 * @param npcId
	 * @param text
	 */
	public NpcSay(int objectId, int messageType, int npcId, String text)
	{
		_objectId = objectId;
		_textType = messageType;
		_npcId = 1000000 + npcId;
		_text = text;
	}
	
	/**
	 * @param objectId 
	 * @param messageType 
	 * @param npcId 
	 * @param text 
	 * @param targetName 
	 */
	public NpcSay(int objectId, int messageType, int npcId, String text, String targetName)
	{
		_objectId = objectId;
		_textType = messageType;
		_npcId = 1000000 + npcId;
		_text = text.contains("$s1") ? text.replace("$s1", targetName) : text;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x02);
		writeD(_objectId);
		writeD(_textType);
		writeD(_npcId);
		writeS(_text);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__02_CREATURESAY;
	}
}