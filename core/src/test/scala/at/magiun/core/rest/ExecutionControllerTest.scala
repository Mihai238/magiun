package at.magiun.core.rest

import at.magiun.core.model.{Execution, ExecutionResult}
import at.magiun.core.service.ExecutionService
import at.magiun.core.{MainModule, UnitTest}
import com.twitter.io.Buf
import io.finch.{Application, Input}

import scala.concurrent.Future

class ExecutionControllerTest extends UnitTest {

  private val mainModule = new MainModule {
    override lazy val executionService: ExecutionService = stub[ExecutionService]
  }

  val stubService: ExecutionService = mainModule.executionService
  val controller: ExecutionController = mainModule.executionController

  it should "create an execution" in {
    val input = Input.post("/executions/").withBody[Application.Json](Buf.Utf8("""{"blockId": "id-3"}"""))
    val result = controller.upsertExecution(input)
    (stubService.execute (_:Execution)) when * returns Future.successful(ExecutionResult("1"))

    val execution = result.awaitValueUnsafe().get
    execution.id should be ("1")
  }

}
