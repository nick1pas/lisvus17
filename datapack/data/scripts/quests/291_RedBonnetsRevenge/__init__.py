# Made by Mr. Have fun! Version 0.2

import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

BLACK_WOLF_PELT = 1482
GRANDMAS_PEARL,GRANDMAS_MIRROR,GRANDMAS_NECKLACE,GRANDMAS_HAIRPIN = range(1502,1506)
SOE=736

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [BLACK_WOLF_PELT]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7553-03.htm" :
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if st.getInt("cond")==0 :
      if st.getPlayer().getLevel() < 4 :
          htmltext = "7553-01.htm"
          st.exitQuest(1)
      else:
          htmltext = "7553-02.htm"
   else :
      if st.getQuestItemsCount(BLACK_WOLF_PELT) < 40 :
        htmltext = "7553-04.htm"
      else:
          htmltext = "7553-05.htm"
          st.exitQuest(1)
          st.playSound("ItemSound.quest_finish")
          st.takeItems(BLACK_WOLF_PELT,-1)
          n = st.getRandom(100)
          if n <= 2 :
            st.giveItems(GRANDMAS_PEARL,1)
          elif n <= 20 :
            st.giveItems(GRANDMAS_MIRROR,1)
          elif n <= 45 :
            st.giveItems(GRANDMAS_NECKLACE,1)
          else :
            st.giveItems(GRANDMAS_HAIRPIN,1)
          st.giveItems(SOE,1)
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("291_RedBonnetsRevenge")
   if st :
     if st.getState() != State.STARTED : return
     if st.getQuestItemsCount(BLACK_WOLF_PELT) < 40 :
       if st.getQuestItemsCount(BLACK_WOLF_PELT) < 39 :
         st.playSound("ItemSound.quest_itemget")
       else:
         st.playSound("ItemSound.quest_middle")
         st.set("cond","2")
       st.giveItems(BLACK_WOLF_PELT,1)
   return

QUEST = Quest(291,"291_RedBonnetsRevenge","Red Bonnets Revenge")
QUEST.addStartNpc(7553)

QUEST.addTalkId(7553)

QUEST.addKillId(317)