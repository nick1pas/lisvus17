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
package net.sf.l2j.gameserver;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.instancemanager.ItemsOnGroundManager;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2World;

public class ItemsAutoDestroy
{
    private static final Logger _log = Logger.getLogger(ItemsAutoDestroy.class.getName());
    
    private final List<L2ItemInstance> _items = new CopyOnWriteArrayList<>();
    
    private ItemsAutoDestroy()
    {
        _log.info("Initializing ItemsAutoDestroy");
        ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(this::removeItems, 5000, 5000);
    }
    
    public static ItemsAutoDestroy getInstance()
    {
        return SingletonHolder._instance;
    }
    
    public synchronized void addItem(L2ItemInstance item)
    {
        item.setDropTime(System.currentTimeMillis());
        _items.add(item);
    }
    
    public synchronized void removeItems()
    {
        if (Config.DEBUG)
            _log.info("[ItemsAutoDestroy] : " + _items.size() + " items to check.");
        
        if (_items.isEmpty())
            return;
        
        long currentTime = System.currentTimeMillis();
        long sleep = Config.AUTODESTROY_ITEM_AFTER > 0 ? Config.AUTODESTROY_ITEM_AFTER * 1000 : 3600000;
        
        for (L2ItemInstance item : _items)
        {
            if (item == null || item.getDropTime() == 0 || item.getLocation() != L2ItemInstance.ItemLocation.VOID)
                _items.remove(item);
            else
            {
                if ((currentTime - item.getDropTime()) > sleep)
                {
                    L2World.getInstance().removeVisibleObject(item, item.getWorldRegion());
                    L2World.getInstance().removeObject(item);
                    
                    _items.remove(item);
                    if (Config.SAVE_DROPPED_ITEM)
                        ItemsOnGroundManager.getInstance().removeObject(item);
                }
            }
        }
        
        if (Config.DEBUG)
            _log.info("[ItemsAutoDestroy] : " + _items.size() + " items remaining.");
    }
    
    private static class SingletonHolder
    {
        protected static final ItemsAutoDestroy _instance = new ItemsAutoDestroy();
    }
}