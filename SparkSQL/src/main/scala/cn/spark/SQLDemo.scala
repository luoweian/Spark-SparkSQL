package cn.spark

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, SQLContext}

/**
  * Created by root on 2016/5/19.
  */
object SQLDemo {



  def main(args: Array[String]) {
    //Config SQLContext
    val conf = new SparkConf().setAppName("SQLDemo")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    //SplitContext by "," and also 实例化
    val personRdd = sc.textFile(args(0)).map(line =>{
      val fields = line.split(",")
      Person(fields(0).toLong, fields(1), fields(2).toInt)
    })

    import sqlContext.implicits._
    val personDf = personRdd.toDF
    //sql
    personDf.registerTempTable("person")
    val df: DataFrame = sqlContext.sql("select * from person where age >= 20 order by age desc limit 2")
    df.write.json(args(1))
    sc.stop()

  }
}
case class Person(id: Long, name: String, age: Int)