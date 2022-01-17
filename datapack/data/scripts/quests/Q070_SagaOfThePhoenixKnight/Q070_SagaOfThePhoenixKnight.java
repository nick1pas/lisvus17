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
package quests.Q070_SagaOfThePhoenixKnight;

import net.sf.l2j.gameserver.model.Location;
import quests.SagaScripts.SagaSuperclass;

/**
 * @author Emperorc
 *
 */
public class Q070_SagaOfThePhoenixKnight extends SagaSuperclass
{
	public static void main(String[] args)
	{
		// Quest class
		new Q070_SagaOfThePhoenixKnight();
	}
	
	public Q070_SagaOfThePhoenixKnight()
	{
		super(70, Q070_SagaOfThePhoenixKnight.class.getSimpleName(), "Saga of the Phoenix Knight");
		
		_classId = 90;
		_prevClassId = 5;
		_npcs = new int[] {7849, 8624, 8277, 7849, 8631, 8646, 8647, 8650, 8654, 8655, 8657, 8277};
		_items = new int[] {7534, 7485, 7268, 7299, 7330, 7361, 7392, 7423, 7093, 6482};
		_mobs = new int[] {5286, 5219, 5278};
		
		_spawnLocs = new Location[]
		{
			new Location(191046, -40640, -3042), 
			new Location(46087, -36372, -1685), 
			new Location(46066, -36396, -1685),
		};
		
		_texts = new String[]
		{
			"PLAYERNAME! Pursued to here! However, I jumped out of the Banshouren boundaries! You look at the giant as the sign of power!",
			"... Oh ... good! So it was ... let's begin!",
			"I do not have the patience ..! I have been a giant force ...! Cough chatter ah ah ah!",
			"Paying homage to those who disrupt the orderly will be PLAYERNAME's death!",
			"Now, my soul freed from the shackles of the millennium, Halixia, to the back side I come ...",
			"Why do you interfere others' battles?",
			"This is a waste of time.. Say goodbye...!",
			"...That is the enemy",
			"...Goodness! PLAYERNAME you are still looking?",
			"PLAYERNAME ... Not just to whom the victory. Only personnel involved in the fighting are eligible to share in the victory.",
			"Your sword is not an ornament. Don't you think, PLAYERNAME?",
			"Goodness! I no longer sense a battle there now.",
			"let...",
			"Only engaged in the battle to bar their choice. Perhaps you should regret.",
			"The human nation was foolish to try and fight a giant's strength.",
			"Must...Retreat... Too...Strong.",
			"PLAYERNAME. Defeat...by...retaining...and...Mo...Hacker",
			"....! Fight...Defeat...It...Fight...Defeat...It..."
		};
		
		// Finally, register all events to be triggered appropriately, using the overridden values.
		registerNPCs();
	}
}