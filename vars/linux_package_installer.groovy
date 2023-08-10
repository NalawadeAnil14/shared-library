#!/usr/bin/env groovy

import org.apache.commons.lang.StringUtils


def call( String package ){
  def distribution = ""
  def packageManager = ""

  def osType = System.getProperty("os.name").toLowerCase() 

  if (osType.contains("linux")) {
    def releaseInfo = new File("/etc/os-release").getText("UTF-8")

    if (releaseInfo.contains("ID=ubuntu") || releaseInfo.contains("ID=debian")) {
        packageManager = "apt-get"
        distribution = "debian"
    } else if (releaseInfo.contains("ID=rhel") || releaseInfo.contains("ID=centos")) {
        packageManager = "yum"
        distribution = "redhat"
    }
  }

  println "Detected package manager ${packageManager}"

  def process = ["dpkg","-l",package].execute()
  def output  = process.text 
  def isPackageInstalled = output.contains("ii ")  

  if(!isPackageInstalled) {
   println "${package} is not installed, Installing"
   
   def installCommand = ""

   if (distribution == "redhat") {
    installCommand = "sudo ${packageManager} install -y ${package}"
   } else if (distribution == "debian") {
    installCommand = "sudo ${packageManager} install -y ${package}"
   }

   if (installCommand) {
    def installProcess = installCommand.execute()
    installProcess.waitFor()
    println "Package ${package} installation completed."
   } else {
        println "Unsupported distribution or package manager."
   } 
  } else { 
    println "Package ${package} is already installed." 
  }
}
