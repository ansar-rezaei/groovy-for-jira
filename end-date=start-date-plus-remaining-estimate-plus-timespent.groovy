import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.IssueInputParameters
import org.apache.log4j.Logger
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder
import com.atlassian.jira.issue.ModifiedValue

def mylog = Logger.getLogger("com.onresolve.jira.groovy")

def userManager = ComponentAccessor.getUserManager()
def customField = ComponentAccessor.getCustomFieldManager().getCustomFieldObject(10202)
def issueManager = ComponentAccessor.getIssueManager()
def user = userManager.getUserByName("Jira Administrator")
def startdate = ComponentAccessor.getCustomFieldManager().getCustomFieldObject("customfield_10201");
def startdatevalue = event.issue.getCustomFieldValue(startdate) as Date
//def oestimate = event.issue.getOriginalEstimate()/(3600*8) as int
def restimate = event.issue.getEstimate()/(3600*8) as int
def timespent = event.issue.getTimeSpent()/(3600*8) as int

int duration = restimate + timespent - 1
    
//def customFieldValue = startdatevalue + duration
    
import java.sql.Timestamp

if (duration == null)
    return
int number = duration.intValue()
Date result = startdatevalue;
Date newDate = new Date(result.getTime())

int i = 0 ;
while (i < number) {
    result = result + 1
    newDate = new Date(result.getTime())
    if (newDate[Calendar.DAY_OF_WEEK] == Calendar.THURSDAY || newDate[Calendar.DAY_OF_WEEK] == Calendar.FRIDAY) {
    	number ++;
       }
    i ++;
    }

def customFieldOldValue = event.issue.getCustomFieldValue(customField)
customField.updateValue(null, event.issue, new ModifiedValue(customFieldOldValue, result), new DefaultIssueChangeHolder())