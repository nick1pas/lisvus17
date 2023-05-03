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
package net.sf.l2j.gameserver.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class ...
 * @version $Revision: 1.5.4.2 $ $Date: 2005/03/27 15:29:33 $
 */
public class L2ClanMember
{
	private static final Logger _log = Logger.getLogger(L2ClanMember.class.getName());
	
	private final L2Clan _clan;
	private int _objectId;
	private String _name;
	private int _level;
	private int _classId;
	private boolean _sex;
	private int _raceOrdinal;
	private int _powerGrade;
	private L2PcInstance _player;
	
	public L2ClanMember(L2Clan clan, String name, int level, int classId, boolean sex, int raceOrdinal, int powerGrade, int objectId)
	{
		_clan = clan;
		_name = name;
		_level = level;
		_classId = classId;
		_sex = sex;
		_raceOrdinal = raceOrdinal;
		_powerGrade = powerGrade;
		_objectId = objectId;
	}
	
	public L2ClanMember(L2PcInstance player)
	{
		_clan = player.getClan();
		_player = player;
		_name = _player.getName();
		_level = _player.getLevel();
		_classId = _player.getClassId().getId();
		_sex = _player.getAppearance().getSex();
		_raceOrdinal = _player.getRace().ordinal();
		_powerGrade = _player.getPowerGrade();
		_objectId = _player.getObjectId();
	}
	
	public void setPlayerInstance(L2PcInstance player)
	{
		if (player == null && _player != null)
		{
			// This is here to keep the data when the player logs off
			_name = _player.getName();
			_level = _player.getLevel();
			_classId = _player.getClassId().getId();
			_sex = _player.getAppearance().getSex();
			_raceOrdinal = _player.getRace().ordinal();
			_powerGrade = _player.getPowerGrade();
			_objectId = _player.getObjectId();
		}
		
		_player = player;
		
	}
	
	public L2Clan getClan()
	{
		return _clan;
	}
	
	public L2PcInstance getPlayerInstance()
	{
		return _player;
	}
	
	public boolean isOnline()
	{
		if (_player == null)
			return false;
		
		if (_player.inOfflineMode())
			return false;
		
		return true;
	}
	
	/**
	 * @return Returns the classId.
	 */
	public int getClassId()
	{
		if (_player != null)
		{
			return _player.getClassId().getId();
		}
		return _classId;
	}
	
	public boolean getSex()
	{
		if (_player != null)
		{
			return _player.getAppearance().getSex();
		}
		return _sex;
	}
	
	public int getRaceOrdinal()
	{
		if (_player != null)
		{
			return _player.getRace().ordinal();
		}
		return _raceOrdinal;
	}
	
	/**
	 * @return Returns the level.
	 */
	public int getLevel()
	{
		if (_player != null)
		{
			return _player.getLevel();
		}
		return _level;
	}
	
	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		if (_player != null)
		{
			return _player.getName();
		}
		return _name;
	}
	
	/**
	 * @return Returns the clan power grade.
	 */
	public int getPowerGrade()
	{
		return _player != null ? _player.getPowerGrade() : _powerGrade;
	}
	
	public void setPowerGrade(int powerGrade)
	{
		_powerGrade = powerGrade;
		
		if (_player != null)
		{
			_player.setPowerGrade(powerGrade);
		}
		else
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("UPDATE characters SET power_grade=? WHERE obj_Id=?"))
			{
				statement.setInt(1, _powerGrade);
				statement.setInt(2, getObjectId());
				statement.executeUpdate();
			}
			catch (Exception e)
			{
				_log.warning("Could not update clan member power grade " + e);
			}
		}
	}
	
	/**
	 * @return Returns the objectId.
	 */
	public int getObjectId()
	{
		if (_player != null)
		{
			return _player.getObjectId();
		}
		return _objectId;
	}
	
	public String getTitle()
	{
		if (_player != null)
		{
			return _player.getTitle();
		}
		return " ";
	}
}