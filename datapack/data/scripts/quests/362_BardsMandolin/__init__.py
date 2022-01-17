# Bards Mandolin Written By MickyLee
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [4316, 4317]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7957_2.htm" :
        st.set("cond","1")
        st.setState(State.STARTED)
        st.playSound("ItemSound.quest_accept")
    elif event == "7957_5.htm" :
        st.giveItems(57,10000)
        st.giveItems(4410,1)
        st.exitQuest(1)
        st.playSound("ItemSound.quest_finish")
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   cond = st.getInt("cond")
   if npcId == 7957 and cond == 0 :
        htmltext = "7957_1.htm"
   elif npcId == 7837 and cond == 1 :
        st.set("cond","2")
        htmltext = "7837_1.htm"
   elif npcId == 7958 and cond == 2 :
        st.set("cond","3")
        st.giveItems(4316,1)
        htmltext = "7958_1.htm"
        st.playSound("ItemSound.quest_itemget")
   elif npcId == 7957 and cond == 3 and st.getQuestItemsCount(4316) and not st.getQuestItemsCount(4317) :
        st.set("cond","4")
        st.giveItems(4317,1)
        htmltext = "7957_3.htm"
   elif npcId == 7957 and cond == 4 and st.getQuestItemsCount(4316) and st.getQuestItemsCount(4317) :
        htmltext = "7957_6.htm"
   elif npcId == 7956 and cond == 4 and st.getQuestItemsCount(4316) and st.getQuestItemsCount(4317) :
        st.takeItems(4316,1)
        st.takeItems(4317,1)
        st.set("cond","5")
        htmltext = "7956_1.htm"
   elif npcId == 7957 and cond == 5 :
        htmltext = "7957_4.htm"
   return htmltext

QUEST = Quest(362,"362_BardsMandolin","Bards Mandolin")
QUEST.addStartNpc(7957)

QUEST.addTalkId(7957)
QUEST.addTalkId(7956)
QUEST.addTalkId(7958)
QUEST.addTalkId(7837)