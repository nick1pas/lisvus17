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
package net.sf.l2j.gameserver.templates;

import java.io.IOException;

import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.handler.SkillHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.holder.SkillHolder;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.conditions.Condition;
import net.sf.l2j.gameserver.skills.conditions.ConditionGameChance;

/**
 * This class is dedicated to the management of weapons.
 * @version $Revision: 1.4.2.3.2.5 $ $Date: 2005/04/02 15:57:51 $
 */
public final class L2Weapon extends L2Item
{
	private final L2WeaponType _type;
	private final int _soulShotCount;
	private final int _spiritShotCount;
	private final int _rndDam;
	private final int _mpConsume;
	private final int _baseAttackRange;

	// Attached skills for Special Abilities
	private SkillHolder[] _skillOnCastHolders;
	private Condition _skillOnCastCondition;
	private SkillHolder[] _skillOnCritHolders;
	private Condition _skillOnCritCondition;

	public L2Skill _castSkill;
	public int _castChance;
	public L2Skill _critSkill;
	public int _critChance;

	/**
	 * Constructor for Weapon.
	 * 
	 * @param set : StatsSet designating the set of couples (key,value) characterizing the armor
	 * @see L2Item constructor
	 */
	public L2Weapon(StatsSet set)
	{
		super(set);

		_type = set.getEnum("weapon_type", L2WeaponType.class, L2WeaponType.NONE);

		_type1 = L2Item.TYPE1_WEAPON_RING_EARRING_NECKLACE;
		_type2 = L2Item.TYPE2_WEAPON;

		_soulShotCount = set.getInteger("soulshots", 0);
		_spiritShotCount = set.getInteger("spiritshots", 0);
		_rndDam = set.getInteger("rnd_dam", 0);
		
		_mpConsume = set.getInteger("mp_consume", 0);
		_baseAttackRange = set.getInteger("attack_range", 40);

		if (set.containsKey("oncast_skill"))
		{
			_skillOnCastHolders = set.getSkillHolderArray("oncast_skill", new SkillHolder[0]);
			if (set.containsKey("oncast_chance"))
			{
				_skillOnCastCondition = new ConditionGameChance(set.getInteger("oncast_chance", 100));
			}
		}

		if (set.containsKey("oncrit_skill"))
		{
			_skillOnCritHolders = set.getSkillHolderArray("oncrit_skill", new SkillHolder[0]);
			if (set.containsKey("oncrit_chance"))
			{
				_skillOnCritCondition = new ConditionGameChance(set.getInteger("oncrit_chance", 100));
			}
		}
	}

	/**
	 * Returns the type of Weapon
	 * @return L2WeaponType
	 */
	@Override
	public L2WeaponType getItemType()
	{
		return _type;
	}

	/**
	 * Returns the ID of the Etc item after applying the mask.
	 * @return int : ID of the Weapon
	 */
	@Override
	public int getItemMask()
	{
		return getItemType().mask();
	}
	
	/**
	 * Returns the quantity of SoulShot used.
	 * @return int
	 */
	public int getSoulShotCount()
	{
		return _soulShotCount;
	}
	
	/**
	 * Returns the quatity of SpiritShot used.
	 * @return int
	 */
	public int getSpiritShotCount()
	{
		return _spiritShotCount;
	}
	
	/**
	 * Returns the random damage inflicted by the weapon
	 * @return int
	 */
	public int getRandomDamage()
	{
		return _rndDam;
	}

	/**
	 * Returns the MP consumption with the weapon
	 * @return int
	 */
	public int getMpConsume()
	{
		return _mpConsume;
	}

	/**
	 * Returns the weapon attack range
	 * @return int
	 */
	public int getBaseAttackRange()
	{
		return _baseAttackRange;
	}

	/**
	 * Returns effects of skills associated with the item to be triggered onHit.
	 * @param caster : L2Character pointing out the caster
	 * @param target : L2Character pointing out the target
	 * @return L2Effect[] : array of effects generated by the skill
	 */
	public L2Effect[] getSkillOnCritEffects(L2Character caster, L2Character target)
	{
		if (_skillOnCritHolders == null)
		{
			return _emptyEffectSet;
		}

		L2ItemInstance weaponInst = caster.getActiveWeaponInstance();
		if (weaponInst == null)
		{
			return _emptyEffectSet;
		}
		
		// Keep old charges here
		int chargedSoulshot = weaponInst.getChargedSoulShot();
		int chargedSpiritshot = weaponInst.getChargedSpiritShot();
		
		// Discharge weapon, so that chance skills do not use ss/sps success bonus
		weaponInst.setChargedSoulShot(L2ItemInstance.CHARGED_NONE);
		weaponInst.setChargedSpiritShot(L2ItemInstance.CHARGED_NONE);

		Env env = new Env();
		env.player = caster;
		env.target = target;

		for (SkillHolder holder : _skillOnCritHolders)
		{
			L2Skill skill = holder.getSkill();
			if (skill == null)
			{
				continue;
			}

			env.skill = skill;

			if (!_skillOnCritCondition.test(env, skill))
			{
				continue;
			}

			L2Character[] targets = new L2Character[] {target};
			try
			{
				// Get the skill handler corresponding to the skill type
				ISkillHandler handler = SkillHandler.getInstance().getSkillHandler(skill.getSkillType());
				// Launch the magic skill and calculate its effects
				if (handler != null)
				{
					handler.useSkill(caster, skill, targets);
				}
				else
				{
					skill.useSkill(caster, targets);
				}
			}
			catch (IOException e)
			{
			}
		}

		// Now, restore old charges
		weaponInst.setChargedSoulShot(chargedSoulshot);
		weaponInst.setChargedSpiritShot(chargedSpiritshot);

		return _emptyEffectSet;
	}

	/**
	 * Returns effects of skills associated with the item to be triggered onCast.
	 * @param caster : L2Character pointing out the caster
	 * @param target : L2Character pointing out the target
	 * @param trigger : L2Skill pointing out the skill triggering this action
	 * @return L2Effect[] : array of effects generated by the skill
	 */
	public L2Effect[] getSkillOnCastEffects(L2Character caster, L2Character target, L2Skill trigger)
	{
		if (_skillOnCastHolders == null)
		{
			return _emptyEffectSet;
		}

		L2ItemInstance weaponInst = caster.getActiveWeaponInstance();
		if (weaponInst == null)
		{
			return _emptyEffectSet;
		}
		
		// Keep old charges here
		int chargedSoulshot = weaponInst.getChargedSoulShot();
		int chargedSpiritshot = weaponInst.getChargedSpiritShot();
		
		// Discharge weapon, so that chance skills do not use ss/sps success bonus
		weaponInst.setChargedSoulShot(L2ItemInstance.CHARGED_NONE);
		weaponInst.setChargedSpiritShot(L2ItemInstance.CHARGED_NONE);

		Env env = new Env();
		env.player = caster;
		env.target = target;
		
		for (SkillHolder holder : _skillOnCastHolders)
		{
			L2Skill skill = holder.getSkill();
			if (skill == null)
			{
				continue;
			}

			env.skill = skill;

			if (!_skillOnCastCondition.test(env, skill))
			{
				continue;
			}

			if (trigger.isOffensive() != skill.isOffensive() || trigger.isMagic() != skill.isMagic())
			{
				continue; // Trigger only same type of skill
			}

			if (trigger.isToggle() && skill.getSkillType() == SkillType.BUFF)
			{
				continue; // No buffing with toggle skills
			}

			L2Character[] targets = new L2Character[] {target};
			try
			{
				// Get the skill handler corresponding to the skill type
				ISkillHandler handler = SkillHandler.getInstance().getSkillHandler(skill.getSkillType());
				// Launch the magic skill and calculate its effects
				if (handler != null)
				{
					handler.useSkill(caster, skill, targets);
				}
				else
				{
					skill.useSkill(caster, targets);
				}
			}
			catch (IOException e)
			{
			}
		}
		
		// Now, restore old charges
		weaponInst.setChargedSoulShot(chargedSoulshot);
		weaponInst.setChargedSpiritShot(chargedSpiritshot);

		return _emptyEffectSet;
	}
}
