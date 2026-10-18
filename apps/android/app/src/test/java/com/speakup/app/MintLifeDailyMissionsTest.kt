package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintLifeDailyMissionsTest {
 @Test fun rejectsUnpublishedAndChildIneligibleTasks(){val a=MintLifeMission("a","Grammar",2,true,true,true);val b=a.copy(id="b",published=false);assertEquals(listOf(a),MintLifeDailyMissions.suggested(listOf(a,b)));assertEquals(2,MintLifeDailyMissions.duration(listOf(a)))}
}
