import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue

// the linked issues with that issue type will used
final String linkedIssueType = "Epic"

final String issueLinkTypeName = "ParentChild"


// the values of that custom field - of type number - we want to sum up
final String BurnedValue = "BBV"
final String EstimatedValue = "EBV"

def linkedIssues = ComponentAccessor.issueLinkManager.getOutwardLinks(issue.id).findAll { it.destinationObject.issueType.name == linkedIssueType }
if (!linkedIssues) {
    return null
}

def mycustomField = ComponentAccessor.customFieldManager.getCustomFieldObjects(linkedIssues.first().destinationObject).findByName(BurnedValue)
def myBurnedValue = ComponentAccessor.customFieldManager.getCustomFieldObjects(linkedIssues.first().destinationObject).findByName(BurnedValue)
def myEstimatedValue = ComponentAccessor.customFieldManager.getCustomFieldObjects(linkedIssues.first().destinationObject).findByName(EstimatedValue)

if (!myBurnedValue || !myEstimatedValue) {
    log.debug "Custom fields are not configured for that context"
    return null
}

def bbv = linkedIssues*.destinationObject.sum {  Issue it -> it.getCustomFieldValue(myBurnedValue) ?: 0 }
def ebv = linkedIssues*.destinationObject.sum {  Issue it -> it.getCustomFieldValue(myEstimatedValue) ?: 0 }

