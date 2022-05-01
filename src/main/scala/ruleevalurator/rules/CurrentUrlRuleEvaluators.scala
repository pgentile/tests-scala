package org.example.testsscala
package ruleevalurator.rules

import ruleevalurator.RuleEvaluator

object CurrentUrlRuleEvaluators {

  private def getCurrentUrl(context: Context) = context.currentUrl

  def eqTo(value: String): RuleEvaluator[Context] = (getCurrentUrl _) andThen StringMatchers.eqTo(value)

  def startsWith(value: String): RuleEvaluator[Context] = (getCurrentUrl _) andThen StringMatchers.startsWith(value)

  def endsWith(value: String): RuleEvaluator[Context] = (getCurrentUrl _) andThen StringMatchers.endsWith(value)

  def contains(value: String): RuleEvaluator[Context] = (getCurrentUrl _) andThen StringMatchers.contains(value)

}
