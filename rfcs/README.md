# Vulcan RFCs

## What is an RFC?

The "RFC" (request for comments) process is intended to provide a
consistent and controlled path for new features to enter the project.

Many changes, including bug fixes and documentation improvements can be
implemented and reviewed via the normal GitHub pull request workflow.

This process is meant for incorporating "significant" updates and give an 
overview of the transitions in their design to those involved in maintaining
or reviewing the project.

## The RFC life-cycle

An RFC goes through the following stages:

- **Pending:** when the RFC is submitted as a PR.
- **Active:** when an RFC PR is merged and undergoing implementation.
- **Landed:** when an RFC's proposed changes are shipped in an actual release.
- **Rejected:** when an RFC PR is closed without being merged.

[Pending RFC List](https://github.com/airavata-courses/vulcan/labels/RFC%20Pending)

## When to follow this process

A "significant" update is loosely defined, but may include the following:

- Adding a new feature, such as a service component.
- Modifying the behavior of an existing service or the interfaces it exposes.
- The removal of features that are already shipped as part of a release.

Some changes do not require an RFC:

- Additions that strictly improve objective, numerical quality criteria (speedup, better runtime support)
- Fixing objectively incorrect behavior
- Rephrasing, reorganizing or refactoring
- Addition or removal of warnings

If you submit a pull request to implement a new feature without going
through the RFC process, it may be closed with a polite request to
submit an RFC first.

## Why to follow this process
Typically, an RFC process is used to reach a consensus among development teams and 
other stakeholders. However, our intention behind this process is focused on bringing 
transparency in the formulation of feature design. The goal is not to emphasize on writing
highly formal specification documents that may constrain development time, but rather 
facilitate an open exchange of ideas through channels such as GitHub Issues, Discussions 
and Pull Requests, and provide a clear transition of proposals and their revisions made
by members that finally make it into an implementation.

We largely prefer offline communication and whiteboard drawings for more abstract 
discussions as their agility cannot be exchanged for the transparency of another medium, 
however any major update mentioned above _must_ ultimately go through the RFC process
to ensure structured design and discussions. We also believe this makes it easier for 
members outside the core team to review or even contribute to the project without 
familiarity with the implementation.

## What the process is

In short, to make any major updates, one must first get the
RFC merged into the RFC directory as a markdown file. At that point the RFC
is 'active' and may be implemented by one of the members.

1. Create a new issue referencing the proposed feature. Assign the appropriate ongoing project (or leave empty if unknown).

    - If the proposal is approved and eventually implemented by the author, the **issue** may be moved to "In Progress" and subsequent stages on the project board. **Pull requests** are always created separately for an RFC and its implementation.

    - If the proposal is implemented by a member other than the author, the issue can be closed after merging the RFC while a new issue would be created by the person assigned with the implementation.

    - Use the RFC lifecycle stage labels only on the issues/pull requests used for merging the RFC (and not the implementation, if it uses a separate issue).

2.  (Optional) Open a new thread in [Discussions](https://github.com/airavata-courses/vulcan/discussions) and make sure to set the category to "RFC Discussions".

    - This is useful if you believe an idea is too abstract and requires input from other members before drafting. Otherwise, such discussions would be part of the issue referenced by the RFC.

3.  Work on a draft proposal in a Markdown file based on the template (`0000-template.md`) found in this directory.

4.  If you believe your proposal has enough information for review:

    - Fork this repo.

    - Create your proposal as `active-rfcs/0000-my-feature.md` (where "my-feature" is descriptive. Don't assign an RFC number yet).

    - Submit a pull request. Link the discussion thread, if applicable.

5.  The [core team] will provide feedback and decide if the feature would be implemented.

    - An RFC can be modified based upon feedback from the [core team] and community.

    - An RFC may be rejected after a discussion has settled and a rationale is provided for rejection. A member of the [core team] should then close the RFC's associated pull request.

    - An RFC may be accepted if the requirements/use cases are handled adequately. A [core team] member will merge the RFC's associated pull request, at which point the RFC will become 'active'.

## Details on Active RFCs

Once an RFC becomes active then authors may implement it and submit the
feature as a pull request to this repo. Becoming 'active' does not mean the feature
will ultimately be merged; the final implementation will go through its own review.

Modifications to active RFCs can be done in followup PRs. We ideally expect RFCs
to reflect the final design of the feature, and while it may not be possible with
every RFC that has been merged, we would try to keep the document in sync with any
major change in the plan.

## Implementing an RFC

The author of an RFC might not necessarily implement it. Of course, the
RFC author (like any other developer) is welcome to post an
implementation for review after the RFC has been accepted.

An active RFC should have the link to the implementation PR listed if there is one. Feedback to the actual implementation should be conducted in the implementation PR instead of the original RFC PR.

## Reviewing RFCs

Members of the [core team] will review open RFC pull requests on a regular basis. If a core team member believes an RFC PR is ready to be accepted into active status, they can approve the PR using GitHub's review feature to signal their approval of the RFC.

**Vulcan's RFC process owes its inspiration to the [Vue RFC process](https://github.com/vuejs/rfcs), [React RFC process](https://github.com/reactjs/rfcs) and [Rust RFC process](https://github.com/rust-lang/rfcs).**

[core team]: https://github.com/airavata-courses/vulcan#core-team
