#Made by Emperorc (adapted for L2JLisvus by roko91)

import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "613_ProveYourCourage_Varka"

#NPC
Ashas = 8377
Hekaton = 10299

#Quest Items
Hekaton_Head = 7240
Valor_Feather = 7229
Varka_Alliance_Three = 7223

def giveReward(st,npc):
    if st.getState() == State.STARTED :
        npcId = npc.getNpcId()
        cond = st.getInt("cond")
        if npcId == Hekaton :
            if st.getPlayer().isAlliedWithVarka() :
                if cond == 1:
                    if st.getPlayer().getAllianceWithVarkaKetra() == -3 and st.getQuestItemsCount(Varka_Alliance_Three) :
                        st.giveItems(Hekaton_Head,1)
                        st.set("cond","2")

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [Hekaton_Head]

 def onEvent (self,event,st) :
   htmltext = event
   if event == "8377-04.htm" :
       if st.getPlayer().getAllianceWithVarkaKetra() == -3 and st.getQuestItemsCount(Varka_Alliance_Three) :
            if st.getPlayer().getLevel() >= 75 :
                    st.set("cond","1")
                    st.setState(State.STARTED)
                    st.playSound("ItemSound.quest_accept")
                    htmltext = "8377-04.htm"
            else :
                htmltext = "8377-03.htm"
                st.exitQuest(1)
       else :
            htmltext = "8377-02.htm"
            st.exitQuest(1)
   elif event == "8377-07.htm" :
       st.takeItems(Hekaton_Head,-1)
       st.giveItems(Valor_Feather,1)
       st.addExpAndSp(10000,0)
       st.playSound("ItemSound.quest_finish")
       htmltext = "8377-07.htm"
       st.exitQuest(1)
   return htmltext

 def onTalk (self,npc,st):
    htmltext = JQuest.getNoQuestMsg()
    if st :
      npcId = npc.getNpcId()
      cond = st.getInt("cond")
      Head = st.getQuestItemsCount(Hekaton_Head)
      Valor = st.getQuestItemsCount(Valor_Feather)
      if npcId == Ashas :
          if Valor == 0 :
              if Head == 0:
                  if cond != 1 :
                      htmltext = "8377-01.htm"
                  else:
                      htmltext = "8377-06.htm"
              else :
                  htmltext = "8377-05.htm"
    return htmltext

 def onKill(self,npc,player,isPet):
    partyMembers = [player]
    party = player.getParty()
    if party :
       partyMembers = party.getPartyMembers()
       for player in partyMembers :
           pst = player.getQuestState(qn)
           if pst :
              giveReward(pst,npc)
    else :
       pst = player.getQuestState(qn)
       if pst :
          giveReward(pst,npc)
    return

QUEST = Quest(613,qn,"Prove Your Courage!")
QUEST.addStartNpc(Ashas)

QUEST.addTalkId(Ashas)

QUEST.addKillId(Hekaton)