import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.customfields.manager.OptionsManager
import com.onresolve.scriptrunner.runner.ScriptRunnerImpl
import com.onresolve.scriptrunner.runner.customisers.PluginModule
import com.onresolve.scriptrunner.runner.customisers.WithPlugin
import java.lang.Object
import com.atlassian.jira.issue.worklog.WorklogImpl2
import java.text.SimpleDateFormat
import com.atlassian.crowd.embedded.api.User
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.worklog.DefaultWorklogManager
import com.atlassian.jira.security.roles.ProjectRoleManager
import com.atlassian.jira.issue.worklog.Worklog
import com.atlassian.jira.datetime.LocalDate
import com.atlassian.jira.issue.worklog.*
import com.atlassian.jira.event.issue.IssueEvent
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.index.IssueIndexingService
import com.atlassian.jira.issue.link.IssueLink
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.issue.link.IssueLinkManager
import com.atlassian.jira.user.ApplicationUser

def issueManager = ComponentAccessor.getIssueManager()
def user= ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
MutableIssue issue = event.issue as MutableIssue

WorklogManager worklogManager = ComponentAccessor.getWorklogManager()
List worklogs = worklogManager.getByIssue(issue)
def last_worklog=worklogs.last()
def author = last_worklog.getAuthorKey()
def dateformat= new SimpleDateFormat("yyyy/MM/dd");
Date date = new Date();
//IssueManager issueManager = ComponentAccessor.getIssueManager();
//def projectRoleManager = ComponentAccessor.getGroupManager()
def projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
//ProjectRoleManager projectRoleManager = issueManager.getComponentInstanceOfType(ProjectRoleManager.class) as ProjectRoleManager
def UsersRole = projectRoleManager.getProjectRole("Users")
long timespent = 1
def last_logged=last_worklog.getTimeSpent()
Calendar cal = Calendar.getInstance();
//cal.add(Calendar.DATE, -30);
Date todate1 = cal.getTime();
def date_limit = dateformat.format(todate1);
def last_worklog_date=dateformat.format(last_worklog.getStartDate())
log.error(dateformat.parse(last_worklog_date).before(dateformat.parse(date_limit)))
if(last_worklog.getTimeSpent()>1 && dateformat.parse(last_worklog_date).before(dateformat.parse(date_limit))){
String comment=last_logged+'sec logged by '+author+' on date '+last_worklog_date
def worklog = new WorklogImpl2(issue, last_worklog.getId(), author, comment, new Date(), null, null,last_logged, UsersRole)
worklogManager.update(issue.reporter, worklog, 0L, true)
}