# Made by Mr. - Version 0.3 by DrLecter
import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

ADAMANTITE_ORE = 1024
BUCKLER = 20

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [ADAMANTITE_ORE]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
       st.set("cond","1")
       st.setState(State.STARTED)
       st.playSound("ItemSound.quest_accept")
       htmltext = "7005-05.htm"
    elif event == "157_1" :
       htmltext = "7005-04.htm"
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   cond = st.getInt("cond")
   if id == State.COMPLETED :
     htmltext = JQuest.getAlreadyCompletedMsg()
   elif cond == 0 :
     if st.getPlayer().getLevel() >= 5 :
        htmltext = "7005-03.htm"
     else :
        htmltext = "7005-02.htm"
        st.exitQuest(1)
   elif cond :
     if st.getQuestItemsCount(ADAMANTITE_ORE) >= 20 :
        st.takeItems(ADAMANTITE_ORE,-1)
        st.setState(State.COMPLETED)
        st.playSound("ItemSound.quest_finish")
        st.giveItems(BUCKLER,1)
        htmltext = "7005-07.htm"
     else :
        htmltext = "7005-06.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("157_RecoverSmuggled")
   if st :
      if st.getState() != State.STARTED : return
      adamantite = st.getQuestItemsCount(ADAMANTITE_ORE)
      if st.getInt("cond") == 1 and adamantite < 20 :
         npcId = npc.getNpcId()
         numItems, chance = divmod(40*Config.RATE_DROP_QUEST,100)
         if st.getRandom(100) <= chance :
            numItems += 1
         numItems = int(numItems)
         if numItems != 0 :
            if 20 <= (adamantite + numItems) :
               numItems = 20 - adamantite
               st.playSound("ItemSound.quest_middle")
               st.set("cond","2")
            else :
               st.playSound("ItemSound.quest_itemget")
            st.giveItems(ADAMANTITE_ORE,numItems)
   return

QUEST = Quest(157,"157_RecoverSmuggled","Recover Smuggled")
QUEST.addStartNpc(7005)

QUEST.addTalkId(7005)

QUEST.addKillId(121)