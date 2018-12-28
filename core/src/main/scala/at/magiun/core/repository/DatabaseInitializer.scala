package at.magiun.core.repository

import scala.concurrent.Await
import scala.concurrent.duration._

class DatabaseInitializer(dataSetRepository: DataSetRepository,
                         blockRepository: BlockRepository) {

  // https://www.kaggle.com/c/titanic/data
  private val sampleCsvUrl = "file://" + getClass.getClassLoader.getResource("titanic.csv").getFile

  // https://www.kaggle.com/wenruliu/adult-income-dataset
  private val incomeCsvUrl = "file://" + getClass.getClassLoader.getResource("income.csv").getFile

  // https://www.kaggle.com/c/talkingdata-mobile-user-demographics
  private lazy val genderAgeUrl = "file://" + getClass.getClassLoader.getResource("datasets/gender_age_train.csv").getFile

  // https://www.kaggle.com/szamil/who-suicide-statistics
  private lazy val suicideUrl = "file://" + getClass.getClassLoader.getResource("datasets/suicide.csv").getFile

  // https://www.kaggle.com/gaborfodor/additional-kiva-snapshot
  private lazy val kivaUrl = "file://" + getClass.getClassLoader.getResource("datasets/country_stats.csv").getFile

  // https://www.kaggle.com/hackerrank/developer-survey-2018
  private lazy val hackerUrl = "file://" + getClass.getClassLoader.getResource("datasets/hacker_rank.csv").getFile

  // https://catalog.data.gov/dataset/nutrition-physical-activity-and-obesity-women-infant-and-child-dfe5d
  private lazy val nutritionUrl = "file://" + getClass.getClassLoader.getResource("datasets/nutrition.csv").getFile


  // https://www.kaggle.com/c/acm-sf-chapter-hackathon-big/data
  private lazy val bigCsvUrl = "file://" + getClass.getClassLoader.getResource("big.csv").getFile

  def init(): Unit = {
    insertDataSet(MagiunDataSetEntity(0, "titanic", "FileCsv", sampleCsvUrl))
    insertDataSet(MagiunDataSetEntity(0, "income", "FileCsv", incomeCsvUrl))
//    insertDataSet(MagiunDataSetEntity(0, "genderAge", "FileCsv", genderAgeUrl))
//    insertDataSet(MagiunDataSetEntity(0, "suicide", "FileCsv", suicideUrl))
//    insertDataSet(MagiunDataSetEntity(0, "hacker", "FileCsv", hackerUrl))
//    insertDataSet(MagiunDataSetEntity(0, "nutrition", "FileCsv", nutritionUrl))

//    insertDataSet(MagiunDataSetEntity(0, "big", "FileCsv", bigCsvUrl))

    insertBlock(BlockEntity("id-2", "FileReader", s"""{"inputs":[],"params":{"fileName":"$sampleCsvUrl"}}"""))
    insertBlock(BlockEntity("id-3", "LinearRegression", """{"inputs":[{"blockId": "id-2", "index": 0}],"params":{"maxIter":"3"}}"""))
    insertBlock(BlockEntity("id-4", "DropColumn", """{"inputs":[{"blockId": "id-2", "index": 0}],"params":{"columnName":"PassengerId"}}"""))
    insertBlock(BlockEntity("id-5", "FileWriter", s"""{"inputs":[{"blockId": "id-4", "index": 0}],"params":{"fileName":"$sampleCsvUrl-tmp"}}"""))
  }

  private def insertDataSet(magiunDataSetEntity: MagiunDataSetEntity): Unit = {
    Await.result(dataSetRepository.upsert(magiunDataSetEntity), 5.seconds)
  }

  private def insertBlock(blockEntity: BlockEntity) = {
    Await.result(blockRepository.upsert(blockEntity), 5.seconds)
  }


}
